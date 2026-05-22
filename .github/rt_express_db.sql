-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Máy chủ: 127.0.0.1
-- Thời gian đã tạo: Th5 22, 2026 lúc 09:23 AM
-- Phiên bản máy phục vụ: 10.4.32-MariaDB
-- Phiên bản PHP: 8.2.12

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Cơ sở dữ liệu: `rt_express_db`
--

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `categories`
--

CREATE TABLE `categories` (
  `id` int(11) NOT NULL,
  `name` varchar(100) NOT NULL,
  `description` text DEFAULT NULL,
  `image_url` varchar(255) DEFAULT NULL,
  `created_at` timestamp NOT NULL DEFAULT current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Đang đổ dữ liệu cho bảng `categories`
--

INSERT INTO `categories` (`id`, `name`, `description`, `image_url`, `created_at`) VALUES
(3, 'Burger', 'Các loại bánh kẹp thịt bò, gà, tôm nướng', 'https://rtexpress.com/img/cat-burger.jpg', '2026-04-18 14:41:54'),
(4, 'Gà Rán', 'Gà rán giòn rụm với công thức đặc biệt', 'https://rtexpress.com/img/cat-chicken.jpg', '2026-04-18 14:41:54'),
(5, 'Đồ Uống', 'Nước ngọt, trà trái cây các loại', 'https://rtexpress.com/img/cat-drinks.jpg', '2026-04-18 14:41:54'),
(6, 'Combo', 'Các suất ăn tiết kiệm kết hợp nhiều món', 'https://rtexpress.com/img/cat-combo.jpg', '2026-04-18 14:41:54');

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `notifications`
--

CREATE TABLE `notifications` (
  `id` int(11) NOT NULL,
  `user_id` int(11) DEFAULT NULL,
  `title` varchar(200) NOT NULL,
  `message` text NOT NULL,
  `is_read` tinyint(1) DEFAULT 0,
  `created_at` timestamp NOT NULL DEFAULT current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Đang đổ dữ liệu cho bảng `notifications`
--

INSERT INTO `notifications` (`id`, `user_id`, `title`, `message`, `is_read`, `created_at`) VALUES
(1, 1, 'Chào mừng bạn!', 'Chào mừng bạn đã đến với R&T Express. Chúc bạn ngon miệng!', 0, '2026-04-18 14:48:07'),
(2, 1, 'Đơn hàng mới', 'Đơn hàng #2 của bạn đã được tiếp nhận và đang chờ cửa hàng xác nhận.', 0, '2026-04-18 14:48:07'),
(3, 10, 'New order received', 'Order #5 has been placed and is pending.', 0, '2026-05-19 13:44:46'),
(4, 10, 'New order received', 'Order #6 has been placed and is pending.', 0, '2026-05-19 14:00:50'),
(5, 10, 'New order received', 'Order #7 has been placed and is pending.', 0, '2026-05-22 07:01:08');

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `orders`
--

CREATE TABLE `orders` (
  `id` int(11) NOT NULL,
  `user_id` int(11) DEFAULT NULL,
  `total_amount` decimal(10,2) NOT NULL,
  `status` enum('PENDING','PREPARING','READY','DELIVERING','DELIVERED','CANCELLED') DEFAULT 'PENDING',
  `delivery_address` text NOT NULL,
  `note` text DEFAULT NULL,
  `created_at` timestamp NOT NULL DEFAULT current_timestamp(),
  `updated_at` timestamp NOT NULL DEFAULT current_timestamp() ON UPDATE current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Đang đổ dữ liệu cho bảng `orders`
--

INSERT INTO `orders` (`id`, `user_id`, `total_amount`, `status`, `delivery_address`, `note`, `created_at`, `updated_at`) VALUES
(3, 1, 70000.00, 'DELIVERED', '123 Đường 3/2, Xuân Khánh, Ninh Kiều, Cần Thơ', NULL, '2026-04-18 14:46:18', '2026-04-18 14:46:18'),
(4, 1, 110000.00, 'PENDING', '456 Nguyễn Văn Cừ, An Khánh, Ninh Kiều, Cần Thơ', NULL, '2026-04-18 14:46:18', '2026-04-18 14:46:18'),
(5, 16, 50000.00, 'PENDING', 'fhjvvy', NULL, '2026-05-19 13:44:46', '2026-05-19 13:44:46'),
(6, 16, 150000.00, 'PENDING', '123dawdaw', NULL, '2026-05-19 14:00:50', '2026-05-19 14:00:50'),
(7, 16, 75000.00, 'PENDING', '470 tran dai nghia', NULL, '2026-05-22 07:01:08', '2026-05-22 07:01:08');

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `order_details`
--

CREATE TABLE `order_details` (
  `id` int(11) NOT NULL,
  `order_id` int(11) DEFAULT NULL,
  `product_id` int(11) DEFAULT NULL,
  `quantity` int(11) NOT NULL,
  `price_at_order` decimal(10,2) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Đang đổ dữ liệu cho bảng `order_details`
--

INSERT INTO `order_details` (`id`, `order_id`, `product_id`, `quantity`, `price_at_order`) VALUES
(5, 3, 2, 1, 45000.00),
(6, 3, 4, 1, 25000.00),
(7, 3, 1, 2, 55000.00),
(8, 5, 21, 2, 25000.00),
(9, 6, 22, 2, 75000.00),
(10, 7, 22, 1, 75000.00);

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `products`
--

CREATE TABLE `products` (
  `id` int(11) NOT NULL,
  `category_id` int(11) DEFAULT NULL,
  `name` varchar(150) NOT NULL,
  `description` text DEFAULT NULL,
  `price` decimal(10,2) NOT NULL,
  `image_url` varchar(255) DEFAULT NULL,
  `is_available` tinyint(1) DEFAULT 1,
  `created_at` timestamp NOT NULL DEFAULT current_timestamp(),
  `updated_at` timestamp NOT NULL DEFAULT current_timestamp() ON UPDATE current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Đang đổ dữ liệu cho bảng `products`
--

INSERT INTO `products` (`id`, `category_id`, `name`, `description`, `price`, `image_url`, `is_available`, `created_at`, `updated_at`) VALUES
(1, 3, 'R&T Special Burger', 'lorem', 55000.00, 'https://upload.wikimedia.org/wikipedia/vi/9/92/KFC_logo-image.png', 1, '2026-04-18 14:39:13', '2026-05-22 06:37:36'),
(2, NULL, 'Beef Burger Đặc Biệt', 'Bò nướng, phô mai lát, xà lách, sốt BBQ', 55000.00, NULL, 1, '2026-04-18 14:41:54', '2026-04-18 14:41:54'),
(3, NULL, 'Burger Gà Giòn', 'Phi lê gà rán, sốt mayonnaise, dưa leo', 45000.00, NULL, 1, '2026-04-18 14:41:54', '2026-04-18 14:41:54'),
(4, NULL, 'Gà Rán Truyền Thống (1 Miếng)', 'Cánh hoặc đùi gà rán nguyên bản', 35000.00, NULL, 1, '2026-04-18 14:41:54', '2026-04-18 14:41:54'),
(5, NULL, 'Khoai Tây Chiên (Vừa)', 'Khoai tây chiên giòn rắc muối', 25000.00, NULL, 1, '2026-04-18 14:41:54', '2026-04-18 14:41:54'),
(6, 3, 'Coca Cola (Lon)', 'Nước giải khát có gas 330ml', 15000.00, NULL, 1, '2026-04-18 14:41:54', '2026-04-18 14:41:54'),
(7, 3, 'Trà Đào Cam Sả', 'Trà đào tươi mát kết hợp cam sả', 25000.00, 'https://upload.wikimedia.org/wikipedia/vi/9/92/KFC_logo-image.png', 1, '2026-04-18 14:41:54', '2026-05-22 06:50:41'),
(8, 4, 'Combo Tiết Kiệm S1', '1 Burger Gà + 1 Khoai tây + 1 Coca', 75000.00, NULL, 1, '2026-04-18 14:41:54', '2026-04-18 14:41:54'),
(9, NULL, 'Beef Burger Đặc Biệt', 'Bò nướng, phô mai lát, xà lách, sốt BBQ', 55000.00, NULL, 1, '2026-04-18 14:43:26', '2026-04-18 14:43:26'),
(10, NULL, 'Burger Gà Giòn', 'Phi lê gà rán, sốt mayonnaise, dưa leo', 45000.00, NULL, 1, '2026-04-18 14:43:26', '2026-04-18 14:43:26'),
(11, NULL, 'Gà Rán Truyền Thống (1 Miếng)', 'Cánh hoặc đùi gà rán nguyên bản', 35000.00, NULL, 1, '2026-04-18 14:43:26', '2026-04-18 14:43:26'),
(12, NULL, 'Khoai Tây Chiên (Vừa)', 'Khoai tây chiên giòn rắc muối', 25000.00, NULL, 1, '2026-04-18 14:43:26', '2026-04-18 14:43:26'),
(13, 3, 'Coca Cola (Lon)', 'Nước giải khát có gas 330ml', 15000.00, NULL, 1, '2026-04-18 14:43:26', '2026-04-18 14:43:26'),
(14, 3, 'Trà Đào Cam Sả', 'Trà đào tươi mát kết hợp cam sả', 25000.00, NULL, 1, '2026-04-18 14:43:26', '2026-04-18 14:43:26'),
(15, 4, 'Combo Tiết Kiệm S1', '1 Burger Gà + 1 Khoai tây + 1 Coca', 75000.00, NULL, 1, '2026-04-18 14:43:26', '2026-04-18 14:43:26'),
(16, NULL, 'Beef Burger Đặc Biệt', 'Bò nướng, phô mai lát, xà lách, sốt BBQ', 55000.00, NULL, 1, '2026-04-18 14:46:18', '2026-04-18 14:46:18'),
(17, NULL, 'Burger Gà Giòn', 'Phi lê gà rán, sốt mayonnaise, dưa leo', 45000.00, NULL, 1, '2026-04-18 14:46:18', '2026-04-18 14:46:18'),
(18, NULL, 'Gà Rán Truyền Thống (1 Miếng)', 'Cánh hoặc đùi gà rán nguyên bản', 35000.00, NULL, 1, '2026-04-18 14:46:18', '2026-04-18 14:46:18'),
(19, NULL, 'Khoai Tây Chiên (Vừa)', 'Khoai tây chiên giòn rắc muối', 25000.00, NULL, 1, '2026-04-18 14:46:18', '2026-04-18 14:46:18'),
(20, 3, 'Coca Cola (Lon)', 'Nước giải khát có gas 330ml', 15000.00, NULL, 1, '2026-04-18 14:46:18', '2026-04-18 14:46:18'),
(21, 3, 'Trà Đào Cam Sả', 'Trà đào tươi mát kết hợp cam sả', 25000.00, 'https://upload.wikimedia.org/wikipedia/vi/9/92/KFC_logo-image.png', 1, '2026-04-18 14:46:18', '2026-05-22 06:52:21'),
(22, 4, 'Combo Tiết Kiệm S1', '1 Burger Gà + 1 Khoai tây + 1 Coca', 75000.00, NULL, 1, '2026-04-18 14:46:18', '2026-04-18 14:46:18');

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `users`
--

CREATE TABLE `users` (
  `id` int(11) NOT NULL,
  `username` varchar(50) NOT NULL,
  `password` varchar(255) NOT NULL,
  `full_name` varchar(100) NOT NULL,
  `email` varchar(100) DEFAULT NULL,
  `phone` varchar(15) NOT NULL,
  `address` text DEFAULT NULL,
  `role` enum('CUSTOMER','STAFF','ADMIN') DEFAULT 'CUSTOMER',
  `fcm_token` text DEFAULT NULL,
  `created_at` timestamp NOT NULL DEFAULT current_timestamp(),
  `updated_at` timestamp NOT NULL DEFAULT current_timestamp() ON UPDATE current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Đang đổ dữ liệu cho bảng `users`
--

INSERT INTO `users` (`id`, `username`, `password`, `full_name`, `email`, `phone`, `address`, `role`, `fcm_token`, `created_at`, `updated_at`) VALUES
(1, 'admin_rt', 'hashed_password_here', 'System Admin', NULL, '0901234567', NULL, 'ADMIN', NULL, '2026-04-18 14:39:13', '2026-04-18 14:39:13'),
(10, 'staff_01', '$2a$12$somehashforstaff', 'Nguyễn Nhân Viên', 'staff01@rtexpress.com', '0903334445', 'Cửa hàng Quận Ninh Kiều', 'STAFF', NULL, '2026-04-18 14:43:26', '2026-04-18 14:43:26'),
(11, 'khachhang01', '$2a$12$somehashforuser', 'Nguyễn Văn A', 'vana@gmail.com', '0912345678', '123 Đường 3/2, Xuân Khánh, Ninh Kiều, Cần Thơ', 'CUSTOMER', NULL, '2026-04-18 14:43:26', '2026-04-18 14:43:26'),
(12, 'khachhang02', '$2a$12$somehashforuser', 'Trần Thị B', 'thib@gmail.com', '0987654321', '456 Nguyễn Văn Cừ, An Khánh, Ninh Kiều, Cần Thơ', 'CUSTOMER', NULL, '2026-04-18 14:43:26', '2026-04-18 14:43:26'),
(13, 'admin', '$2b$10$.7uFWdyFIIM.sn5r/a9wg.MeAj8sjMFU/0I35WSw4iWlv1L/HdsJe', 'System Admin', 'admin@example.com', '0123456789', 'Head Office', 'ADMIN', NULL, '2026-04-18 15:20:58', '2026-04-18 15:20:58'),
(14, 'test_user_1778841222828', '$2b$10$..190b/1UNeqiOVGN3.6lulGoTe5s8fhsLYQRWAjoHOv63Wiu5zq6', 'Test User', 'test_1778841222828@example.com', '0912345678', NULL, 'CUSTOMER', NULL, '2026-05-15 10:33:46', '2026-05-15 10:33:46'),
(16, 'riyaki', '$2b$10$N1dYpgjQflbj5vX4YFHEI.7rOv6HlO6ZMnR3R0iR.wJxub9J2DWKS', 'Huỳnh Ngọc Bảo Trường', 'truongyasuo@gmail.com', '0819033106', '11 leloi', 'CUSTOMER', NULL, '2026-05-15 11:13:19', '2026-05-15 11:13:19');

--
-- Chỉ mục cho các bảng đã đổ
--

--
-- Chỉ mục cho bảng `categories`
--
ALTER TABLE `categories`
  ADD PRIMARY KEY (`id`);

--
-- Chỉ mục cho bảng `notifications`
--
ALTER TABLE `notifications`
  ADD PRIMARY KEY (`id`),
  ADD KEY `idx_notifications_user_id` (`user_id`);

--
-- Chỉ mục cho bảng `orders`
--
ALTER TABLE `orders`
  ADD PRIMARY KEY (`id`),
  ADD KEY `idx_orders_user_id` (`user_id`),
  ADD KEY `idx_orders_status` (`status`);

--
-- Chỉ mục cho bảng `order_details`
--
ALTER TABLE `order_details`
  ADD PRIMARY KEY (`id`),
  ADD KEY `fk_detail_order` (`order_id`),
  ADD KEY `fk_detail_product` (`product_id`);

--
-- Chỉ mục cho bảng `products`
--
ALTER TABLE `products`
  ADD PRIMARY KEY (`id`),
  ADD KEY `idx_products_category_id` (`category_id`),
  ADD KEY `idx_products_is_available` (`is_available`);

--
-- Chỉ mục cho bảng `users`
--
ALTER TABLE `users`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `username` (`username`),
  ADD UNIQUE KEY `email` (`email`);

--
-- AUTO_INCREMENT cho các bảng đã đổ
--

--
-- AUTO_INCREMENT cho bảng `categories`
--
ALTER TABLE `categories`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=15;

--
-- AUTO_INCREMENT cho bảng `notifications`
--
ALTER TABLE `notifications`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=6;

--
-- AUTO_INCREMENT cho bảng `orders`
--
ALTER TABLE `orders`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=8;

--
-- AUTO_INCREMENT cho bảng `order_details`
--
ALTER TABLE `order_details`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=11;

--
-- AUTO_INCREMENT cho bảng `products`
--
ALTER TABLE `products`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=23;

--
-- AUTO_INCREMENT cho bảng `users`
--
ALTER TABLE `users`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=17;

--
-- Các ràng buộc cho các bảng đã đổ
--

--
-- Các ràng buộc cho bảng `notifications`
--
ALTER TABLE `notifications`
  ADD CONSTRAINT `fk_notification_user` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`) ON DELETE CASCADE;

--
-- Các ràng buộc cho bảng `orders`
--
ALTER TABLE `orders`
  ADD CONSTRAINT `fk_order_user` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`) ON DELETE CASCADE;

--
-- Các ràng buộc cho bảng `order_details`
--
ALTER TABLE `order_details`
  ADD CONSTRAINT `fk_detail_order` FOREIGN KEY (`order_id`) REFERENCES `orders` (`id`) ON DELETE CASCADE,
  ADD CONSTRAINT `fk_detail_product` FOREIGN KEY (`product_id`) REFERENCES `products` (`id`) ON DELETE SET NULL;

--
-- Các ràng buộc cho bảng `products`
--
ALTER TABLE `products`
  ADD CONSTRAINT `fk_product_category` FOREIGN KEY (`category_id`) REFERENCES `categories` (`id`) ON DELETE SET NULL;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
