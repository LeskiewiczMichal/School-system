/** @type {import('tailwindcss').Config} */
module.exports = {
  content: ["./src/**/*.{js,jsx,ts,tsx}"],
  theme: {
    extend: {
      colors: {
        primary: "var(--primary)",
        primaryLighter: "var(--primary-lighter)",
        primaryDarkened: "var(--primary-darkened)",
        secondary: "var(--secondary)",
        blueLighter: "var(--blue-lighter)",
        grey: "var(--grey)",
        lemon: "var(--lemon)",
        cyan: "var(--cyan)",
        glaucous: "var(--glaucous)",

        brandMain: "var(--brand-main)",
        brandMainSoft: "var(--brand-main-soft)",
        brandMainLight: "var(--brand-main-light)",
        brandMainBright: "var(--brand-main-bright)",
        brandMainActive: "var(--brand-main-active)",
        brandMainDark: "var(--brand-main-dark)",
        brandMainNearlyBlack: "var(--brand-main-nearly-black)",
        linkBlue: "var(--link-blue)",
        linkDisabled: "var(--link-disabled)",
        grayscaleSlightlyGray: "var(--grayscale-slightly-gray)",
        grayscaleLight: "var(--grayscale-light)",
        grayscaleMedium: "var(--grayscale-medium)",
        grayscaleBackgroundBox: "var(--grayscale-background-box)",
        grayscaleMediumDark: "var(--grayscale-medium-dark)",
        grayscaleDark: "var(--grayscale-dark)",
        grayscaleDarkText: "var(--grayscale-dark-text)",
        hoverGray: "var(--hover-gray)",
      },
    },
  },
  plugins: [],
};
