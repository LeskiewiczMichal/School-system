import { useAppDispatch } from "../../../hooks";
import { useState } from "react";
import { ReactComponent as Logo } from "../../../assets/logo.svg";

export default function LoginForm() {
  const dispatch = useAppDispatch();
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");

  const handleChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    const { name, value } = e.target;
    if (name === "email") {
      setEmail(value);
    } else {
      setPassword(value);
    }
  };

  return (
    <form className="self-center w-96 border p-8 pt-0">
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
      <div className="flex mb-2 items-center justify-between">
        <button
          className="px-4 py-2 font-bold text-white bg-primary rounded hover:bg-blue-700 focus:outline-none focus:shadow-outline"
          type="button"
        >
          Sign In
        </button>
        <a
          className="inline-block text-sm font-bold text-primary align-baseline hover:text-blue-800"
          href="#"
        >
          Forgot Password?
        </a>
      </div>
      <div className="flex items-center">
        <button
          className="w-full px-4 py-2 font-bold text-white bg-primary rounded hover:bg-blue-700 focus:outline-none focus:shadow-outline"
          type="button"
        >
          Try out on pre-made account
        </button>
      </div>
    </form>
  );
}
