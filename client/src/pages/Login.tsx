import { LoginForm } from "../features/authentication";
import { Sidebar } from "../features/sidebar";
import { useAppSelector } from "../hooks";

export default function Login() {
  const mobileNavBar = useAppSelector(
    (state) => state.integration.mobileNavView,
  );

  return (
    <main
      className="min-h-screen w-screen flex justify-center"
      style={{
        backgroundImage: `url(${require("../assets/images/pexels2/entrence.webp")})`,
        backgroundSize: "cover",
        backgroundPosition: "center",
      }}
    >
      {mobileNavBar && <Sidebar />}
      <LoginForm />
    </main>
  );
}
