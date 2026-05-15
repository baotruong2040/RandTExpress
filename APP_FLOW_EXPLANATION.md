# Giải thích chi tiết Luồng hoạt động của App RandTExpress

Tài liệu này giải thích chi tiết cách ứng dụng RandTExpress vận hành, từ lúc khởi chạy cho đến các luồng xử lý dữ liệu cụ thể, dựa trên kiến trúc **Clean Architecture**.

---

## 1. Cấu trúc Tổng quan (Architecture)

Dự án tuân thủ Clean Architecture với các lớp (layers) sau:

*   **UI Layer (Jetpack Compose):** Hiển thị giao diện và nhận tương tác người dùng.
*   **Presentation Layer (ViewModel):** Quản lý trạng thái UI (UiState) và xử lý logic hiển thị.
*   **Domain Layer (Repository Interfaces & Models):** Chứa logic nghiệp vụ trừu tượng.
*   **Data Layer (Repository Impl, API, Database, Preferences):** Thực thi việc lấy dữ liệu từ Server hoặc Local.

---

## 2. Luồng Khởi chạy (App Startup Flow)

### Bước 1: `MainActivity.kt`
Đây là điểm vào (entry point) duy nhất của ứng dụng.
- **`@AndroidEntryPoint`**: Đánh dấu để Hilt có thể inject các phụ thuộc vào Activity.
- **`onCreate()`**: Hàm khởi tạo.
- **`setContent { ... }`**: Thiết lập giao diện bằng Compose. Nó gọi `AppNavigation()`.

### Bước 2: `AppNavigation.kt`
Quyết định xem người dùng sẽ thấy màn hình nào đầu tiên.
- **`sessionViewModel: SessionViewModel = hiltViewModel()`**: Hilt tự động cung cấp ViewModel quản lý phiên đăng nhập.
- **`sessionData.collectAsStateWithLifecycle()`**: Lắng nghe trạng thái đăng nhập từ `DataStore` (Token).
- **Logic**:
    - Nếu `token` trống: Gọi `AuthNavigation()` (Hiển thị màn hình Login/Register).
    - Nếu `token` hợp lệ: Gọi `HomeScreen()` (Vào giao diện chính).

---

## 3. Luồng Xử lý Dữ liệu (Ví dụ: Đăng nhập - Login Flow)

Dưới đây là chi tiết từng bước khi người dùng thực hiện đăng nhập:

### A. UI Layer (`LoginScreen.kt`)
1.  Người dùng nhập Username/Password vào các `OutlinedTextField`.
2.  Mỗi lần nhập, hàm `viewModel::onLoginUsernameChange` được gọi để cập nhật State trong ViewModel.
3.  Khi nhấn nút **"Đăng nhập"**, hàm `viewModel.login()` (trong `scope.launch`) được kích hoạt.

### B. Presentation Layer (`AuthViewModel.kt`)
1.  **`login()`**: Là một `suspend function` chạy trong Coroutine.
2.  Nó kiểm tra tính hợp lệ cơ bản (không để trống).
3.  Cập nhật `isLoading = true` vào `_loginUiState`. Điều này làm UI hiển thị vòng xoay loading.
4.  Gọi `authRepository.login(...)` và đợi kết quả.

### C. Domain Layer (`AuthRepository.kt`)
- Đây là một Interface định nghĩa hàm `login`. Nó không quan tâm dữ liệu lấy từ đâu (API hay Mock).
- Giúp tách biệt logic nghiệp vụ khỏi chi tiết triển khai.

### D. Data Layer (`AuthRepositoryImpl.kt` & `AuthApiService.kt`)
1.  **`AuthRepositoryImpl`**: Triển khai interface của Domain.
2.  Nó tạo một `LoginRequest` object từ tham số truyền vào.
3.  Gọi `authApiService.login(request)`. Đây là nơi **Retrofit** thực hiện gọi HTTP request tới Backend Node.js.
4.  Nhận phản hồi từ Server, nếu thành công thì trả về `AuthResponse` chứa `token`.

### E. Kết thúc & Chuyển hướng
1.  Tại `AuthViewModel`, sau khi nhận `token`, nó gọi `userPreferences.saveSession(token, ...)`.
2.  **`UserPreferences`**: Sử dụng **Jetpack DataStore** để lưu Token xuống bộ nhớ máy một cách an toàn và bất đồng bộ.
3.  Vì `AppNavigation` đang lắng nghe (collect) từ `UserPreferences`, ngay khi Token được lưu, `AppNavigation` sẽ tự động nhận biết và chuyển từ màn hình Login sang `HomeScreen`.

---

## 4. Các thành phần quan trọng khác

### Dependency Injection (Hilt)
- Thư mục `di/`: Chứa các Module (như `NetworkModule.kt`, `RepositoryModule.kt`) hướng dẫn Hilt cách tạo ra các đối tượng như `Retrofit`, `AuthApiService`, hay `AuthRepositoryImpl`.
- Giúp code sạch hơn, dễ test và dễ thay đổi linh kiện mà không ảnh hưởng code ở nơi khác.

### State Management (StateFlow)
- Toàn bộ dữ liệu hiển thị trên màn hình đều được đóng gói trong một `UiState` (data class) duy nhất.
- UI chỉ đơn thuần "phản chiếu" những gì có trong `UiState`. Khi `UiState` thay đổi, Compose tự động vẽ lại (recomposition) những phần cần thiết.

---

## Tóm tắt Luồng gọi Hàm (Sequence)

`LoginScreen` (Click) 
  ➜ `AuthViewModel.login()` 
    ➜ `AuthRepository.login()` (Interface)
      ➜ `AuthRepositoryImpl.login()` 
        ➜ `AuthApiService.login()` (Retrofit Call)
          ➜ **SERVER (Node.js)**
        ➜ Nhận kết quả
      ➜ Trả về ViewModel
    ➜ `UserPreferences.saveSession()` (Lưu Token)
  ➜ `AppNavigation` (Tự động cập nhật nhờ Flow)
➜ Hiển thị `HomeScreen`
