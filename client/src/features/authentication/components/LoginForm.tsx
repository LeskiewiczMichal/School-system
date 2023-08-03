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
    <form className="self-center w-96">
      <Logo style={{ maxWidth: "250px", maxHeight: "250px" }} />
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
    </form>
  );
}
