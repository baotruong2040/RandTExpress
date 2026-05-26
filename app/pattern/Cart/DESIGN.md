---
name: R&T Express Design System
colors:
  surface: '#fcf9f8'
  surface-dim: '#dcd9d9'
  surface-bright: '#fcf9f8'
  surface-container-lowest: '#ffffff'
  surface-container-low: '#f6f3f2'
  surface-container: '#f0eded'
  surface-container-high: '#eae7e7'
  surface-container-highest: '#e5e2e1'
  on-surface: '#1c1b1b'
  on-surface-variant: '#5d403a'
  inverse-surface: '#313030'
  inverse-on-surface: '#f3f0ef'
  outline: '#916f69'
  outline-variant: '#e6bdb6'
  surface-tint: '#be0d00'
  primary: '#bd0d00'
  on-primary: '#ffffff'
  primary-container: '#e32d19'
  on-primary-container: '#ffffff'
  inverse-primary: '#ffb4a7'
  secondary: '#8f4e00'
  on-secondary: '#ffffff'
  secondary-container: '#fe9832'
  on-secondary-container: '#683700'
  tertiary: '#006b4a'
  on-tertiary: '#ffffff'
  tertiary-container: '#00875f'
  on-tertiary-container: '#ffffff'
  error: '#ba1a1a'
  on-error: '#ffffff'
  error-container: '#ffdad6'
  on-error-container: '#93000a'
  primary-fixed: '#ffdad4'
  primary-fixed-dim: '#ffb4a7'
  on-primary-fixed: '#400100'
  on-primary-fixed-variant: '#920800'
  secondary-fixed: '#ffdcc2'
  secondary-fixed-dim: '#ffb77a'
  on-secondary-fixed: '#2e1500'
  on-secondary-fixed-variant: '#6d3a00'
  tertiary-fixed: '#7afac3'
  tertiary-fixed-dim: '#5cdda8'
  on-tertiary-fixed: '#002114'
  on-tertiary-fixed-variant: '#005138'
  background: '#fcf9f8'
  on-background: '#1c1b1b'
  surface-variant: '#e5e2e1'
typography:
  headline-xl:
    fontFamily: Plus Jakarta Sans
    fontSize: 32px
    fontWeight: '800'
    lineHeight: 40px
    letterSpacing: -0.02em
  headline-lg:
    fontFamily: Plus Jakarta Sans
    fontSize: 24px
    fontWeight: '700'
    lineHeight: 32px
  headline-lg-mobile:
    fontFamily: Plus Jakarta Sans
    fontSize: 22px
    fontWeight: '700'
    lineHeight: 28px
  body-md:
    fontFamily: Inter
    fontSize: 16px
    fontWeight: '400'
    lineHeight: 24px
  body-sm:
    fontFamily: Inter
    fontSize: 14px
    fontWeight: '400'
    lineHeight: 20px
  label-lg:
    fontFamily: Plus Jakarta Sans
    fontSize: 14px
    fontWeight: '600'
    lineHeight: 20px
  price-display:
    fontFamily: Plus Jakarta Sans
    fontSize: 20px
    fontWeight: '800'
    lineHeight: 24px
rounded:
  sm: 0.25rem
  DEFAULT: 0.5rem
  md: 0.75rem
  lg: 1rem
  xl: 1.5rem
  full: 9999px
spacing:
  base: 8px
  container-margin: 16px
  gutter: 12px
  stack-sm: 4px
  stack-md: 16px
  stack-lg: 24px
---

## Brand & Style

The design system for this food delivery platform is built around the core values of speed, freshness, and accessibility. The brand personality is energetic and friendly, designed to stimulate appetite and provide a seamless ordering experience.

The visual style is a fusion of **Modern Minimalism** and **Glassmorphism**. It utilizes clean layouts with heavy white space to focus on high-quality food photography, while employing translucent, frosted-glass layers for functional overlays and UI cards. This creates a sense of depth and sophistication that differentiates the product in a crowded market. The interface is specifically optimized for Android, following logical touch patterns and a clear hierarchical flow.

## Colors

The palette is anchored by a high-energy "Crave Red" and "Zesty Orange," colors traditionally associated with the fast-food industry to evoke hunger and excitement.

- **Primary (Crave Red):** Used for main actions, critical branding, and urgent notifications.
- **Secondary (Zesty Orange):** Used for accents, secondary buttons, and promotional highlights.
- **Tertiary (Fresh Mint):** Specifically reserved for "Success" states, "Add to Cart" confirmations, and healthy food categories.
- **Neutral:** A range of deep charcoals and soft grays ensure high legibility against the vibrant brand colors.
- **Glass Base:** A semi-transparent white (#FFFFFFCC) with a 20px background blur for all frosted-glass components.

## Typography

This design system uses **Plus Jakarta Sans** for headlines and labels to provide a friendly, modern, and high-energy feel. **Inter** is used for body text and long descriptions due to its exceptional readability at small sizes on mobile screens.

Headlines utilize tight letter-spacing and heavy weights to create a sense of "Express" urgency. Pricing information is given a specific "price-display" style, ensuring it stands out as a primary piece of information for the user.

## Layout & Spacing

The layout follows a **Fluid Grid** model optimized for the Android viewport. 

- **Grid:** A 4-column grid for mobile with 16px side margins. 
- **Vertical Rhythm:** Built on an 8px baseline. Elements are spaced in multiples of 8 (8, 16, 24, 32) to maintain a consistent density.
- **Safe Areas:** Navigation bars and action buttons must respect Android system gestures and chin heights.
- **Card Spacing:** Item cards within a horizontal scroll use a 12px gutter to imply "more content" off-screen.

## Elevation & Depth

Hierarchy is established through **Backdrop Blurs** and **Ambient Shadows**.

1.  **Level 0 (Base):** Flat background, usually white or a very light gray.
2.  **Level 1 (Cards):** Soft, extra-diffused shadows (Offset: 0, 4; Blur: 20; Opacity: 6% Black) with 1px low-contrast light borders.
3.  **Level 2 (Glass Overlays):** 20px backdrop blur with a 15% opacity white fill. These are used for navigation bars and modal overlays to keep the context of the food imagery behind them.
4.  **Level 3 (Primary Actions):** Floating Action Buttons (FABs) and "Order Now" buttons use a slightly stronger shadow to indicate they are the highest priority interactive elements.

## Shapes

The shape language is consistently **Rounded**, reflecting the approachable and friendly nature of the brand.

- **Standard Cards:** 1rem (16px) corner radius.
- **Input Fields:** 0.75rem (12px) corner radius.
- **Primary Action Buttons:** Fully pill-shaped (2rem+) to distinguish them from other UI containers.
- **Small Chips/Labels:** 0.5rem (8px) corner radius.

## Components

### Buttons
- **Primary:** Pill-shaped, Crave Red background, white bold text.
- **Secondary:** Pill-shaped, ghost style with Zesty Orange border and text.
- **Add to Cart:** Utilizes the Fresh Mint color to provide a positive, distinct feedback loop.

### Cards
Food cards should feature a large, high-quality image at the top with a 16px radius. The content area below uses semi-transparent backgrounds when placed over images (Glassmorphism). Include a prominent "+" button in the bottom right corner for quick adding.

### Input Fields
Underlined or softly boxed. In "Glass Mode" (e.g., Login/Sign-up screens), use white outlines with 40% opacity and blurred backgrounds for maximum legibility over complex background images.

### Chips & Tags
Used for categories (e.g., "Burger", "Promo", "Fast Delivery"). These use the secondary color at 10% opacity with a solid secondary-colored text for a "soft" interactive look.

### Bottom Navigation
A persistent glassmorphic bar at the bottom of the screen with clear icon/label combinations. The active state is highlighted with Crave Red.