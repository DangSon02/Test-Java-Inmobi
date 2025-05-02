import React, { useState } from "react";
import AuthForm from "./components/Auth";
import HomePage from "./HomePage";
import Background from "./images/free/bg1.png";
import gui from "./util/gui";

function App() {
  const [isLogged, setIsLogged] = useState(!!localStorage.getItem("token"));
  return (
    <div
      style={{
        backgroundColor: "#333",
        width: gui.screenWidth,
        height: gui.screenHeight,
        minHeight: 896,
        overflow: "hidden",
        display: "flex",
        position: "relative",
        alignItems: "center",
        flexDirection: "column",
        backgroundImage: `url(${Background})`,
        backgroundRepeat: "no-repeat",
        backgroundSize: "cover",
      }}
    >
      {isLogged ? <HomePage /> : <AuthForm setIsLogged={setIsLogged} />}
    </div>
  );
}

export default App;
