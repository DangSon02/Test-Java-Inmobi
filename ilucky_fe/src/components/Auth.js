import React, { useState } from "react";
import { callApi, mainUrl } from "../util/api/requestUtils";

const AuthForm = (props) => {
  const [isLogin, setIsLogin] = useState(true);
  const [username, setUsername] = useState("");
  const [password, setPassword] = useState("");
  const [message, setMessage] = useState("");

  const toggleForm = () => {
    setIsLogin(!isLogin);
    setMessage("");
    setUsername("");
    setPassword("");
  };

  const handleSubmit = async (e) => {
    e.preventDefault();

    if (isLogin) {
      const url = mainUrl + "/api/auth/login";
      await callApi(url, "POST", {
        username,
        password,
      })
        .then((data) => {
          if (data) {
            localStorage.setItem("token", data.accessToken);
            props.setIsLogged(true);
          }
        })
        .catch((error) => {
          console.log({ error });
          setMessage("Invalid username and password");
        });
    } else {
      const url = mainUrl + "/api/auth/register";
      await callApi(url, "POST", {
        username,
        password,
      });
      setMessage("Register successfully");
      setIsLogin(true);
    }
  };

  const styles = {
    container: {
      maxWidth: "350px",
      margin: "60px auto",
      padding: "20px",
      borderRadius: "8px",
      boxShadow: "0 0 15px rgba(0,0,0,0.1)",
      fontFamily: "Arial, sans-serif",
      backgroundColor: "#fff",
    },
    heading: {
      textAlign: "center",
      marginBottom: "20px",
    },
    formGroup: {
      marginBottom: "15px",
    },
    label: {
      display: "block",
      marginBottom: "5px",
      fontWeight: "bold",
    },
    input: {
      width: "100%",
      padding: "8px",
      borderRadius: "4px",
      border: "1px solid #ccc",
    },
    button: {
      width: "100%",
      padding: "10px",
      marginTop: "10px",
      backgroundColor: "#007bff",
      border: "none",
      color: "#fff",
      fontWeight: "bold",
      borderRadius: "4px",
      cursor: "pointer",
    },
    toggleButton: {
      marginTop: "15px",
      background: "none",
      border: "none",
      color: "#007bff",
      textDecoration: "underline",
      cursor: "pointer",
    },
    message: {
      textAlign: "center",
      marginTop: "10px",
      fontWeight: "bold",
      color: "black",
    },
  };

  return (
    <div style={styles.container}>
      <h2 style={styles.heading}>{isLogin ? "Login" : "Register"}</h2>
      <form onSubmit={handleSubmit}>
        <div style={styles.formGroup}>
          <label style={styles.label}>Username:</label>
          <input
            style={styles.input}
            type="text"
            required
            value={username}
            onChange={(e) => setUsername(e.target.value)}
          />
        </div>
        <div style={styles.formGroup}>
          <label style={styles.label}>Password:</label>
          <input
            style={styles.input}
            type="password"
            required
            value={password}
            onChange={(e) => setPassword(e.target.value)}
          />
        </div>
        <button style={styles.button} type="submit">
          {isLogin ? "Login" : "Register"}
        </button>
      </form>
      {message && <p style={styles.message}>{message}</p>}
      <button onClick={toggleForm} style={styles.toggleButton}>
        {isLogin
          ? "Need an account? Register"
          : "Already have an account? Login"}
      </button>
    </div>
  );
};

export default AuthForm;
