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
      },
    },
  },
  plugins: [],
};
