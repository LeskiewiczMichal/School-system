import { LoginForm } from "../features/authentication";

export default function Login() {
  return (
    <main
      className="min-h-screen w-screen flex justify-center"
      style={{
        backgroundImage: `url(${require("../assets/images/pexels2/entrence.webp")})`,
        backgroundSize: "cover",
        backgroundPosition: "center",
      }}
    >
      <LoginForm />
    </main>
  );
}
