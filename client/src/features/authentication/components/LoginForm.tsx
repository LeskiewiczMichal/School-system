import { useAppDispatch } from "../../../hooks";
import { useState } from "react";
import { ReactComponent as Logo } from "../../../assets/logo.svg";
import login from "../services/login";
import { useNavigate } from "react-router-dom";

export default function LoginForm() {
  const navigate = useNavigate();
  const dispatch = useAppDispatch();
  // Form data
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const [error, setError] = useState<null | string>(null);

  const handleChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    const { name, value } = e.target;
    if (name === "email") {
      setEmail(value);
    } else {
      setPassword(value);
    }
  };

  const handleLogin = async (e: React.MouseEvent<HTMLButtonElement>) => {
    e.preventDefault();

    try {
      const loginRequest = { email, password };
      await dispatch(login(loginRequest));

      return navigate("/");
    } catch (e: any) {
      setError(e.message);
    }
  };

  return (
    <form className="self-center w-96 lg:w-1/3 border p-8 pt-0 bg-grey rounded">
      <Logo
        style={{
          maxWidth: "100%",
          maxHeight: "250px",
          backgroundSize: "cover",
        }}
      />
      <div className="mb-4">
        <label
          className="block mb-2 text-sm font-bold text-gray-700"
          htmlFor="email"
        >
          Email
        </label>
        <input
          className="w-full px-3 py-2 text-sm leading-tight text-gray-700 border rounded shadow appearance-none focus:outline-none focus:shadow-outline"
          id="email"
          name="email"
          type="email"
          placeholder="Email"
          value={email}
          onChange={handleChange}
        />
      </div>
      <div className="mb-6">
        <label
          className="block mb-2 text-sm font-bold text-gray-700"
          htmlFor="password"
        >
          Password
        </label>
        <input
          className="w-full px-3 py-2 mb-3 text-sm leading-tight text-gray-700 border rounded shadow appearance-none focus:outline-none focus:shadow-outline"
          id="password"
          name="password"
          type="password"
          placeholder="******************"
          value={password}
          onChange={handleChange}
        />
      </div>
      {error && (
        <div className="mb-6">
          <p className="text-red-500">{error}</p>
        </div>
      )}
      <div className="flex mb-2 items-center justify-between">
        <button
          className="px-4 py-2 font-bold text-white bg-primary rounded hover:bg-primaryLighter focus:outline-none focus:shadow-outline"
          type="button"
          onClick={handleLogin}
        >
          Sign In
        </button>
        <a
          className="inline-block text-sm font-bold text-primary align-baseline hover:text-primaryLighter"
          href="#"
        >
          Forgot Password?
        </a>
      </div>
      <div className="flex items-center">
        <button
          className="w-full px-4 py-2 font-bold text-white bg-primary rounded hover:bg-primaryLighter focus:outline-none focus:shadow-outline"
          type="button"
        >
          Try out on pre-made account
        </button>
      </div>
    </form>
  );
}
