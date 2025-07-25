import { useState } from "react";
import { useNavigate } from "react-router-dom";

export default function Login() {
  const [message, setMessage] = useState("");
  const navigate = useNavigate();

  const handleLogin = async (e) => {
    e.preventDefault();

    const body = {
      username: e.target.username.value,
      password: e.target.password.value,
    };

    try {
      const res = await fetch("http://localhost:8080/login", {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify(body),
      });

      if (!res.ok) {
        setMessage("Login failed");
        return;
      }

      const token = await res.text(); // assuming backend returns plain JWT
      sessionStorage.setItem("jwt", token);
      setMessage("Login successful");
      navigate("/dashboard");
    } catch (err) {
      console.error(err);
      setMessage("Login failed");
    }
  };

  return (
    <div style={{ padding: "2rem", maxWidth: "300px", margin: "auto" }}>
      <form onSubmit={handleLogin}>
        <h3>Login</h3>
        <input name="username" type="text" placeholder="Username" required /><br /><br />
        <input name="password" type="password" placeholder="Password" required /><br /><br />
        <button type="submit">Login</button>
      </form>
      <p>{message}</p>
    </div>
  );
}
