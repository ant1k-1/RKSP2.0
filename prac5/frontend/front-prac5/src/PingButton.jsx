import React, { useState } from "react";

function PingButton() {
  const [buttonText, setButtonText] = useState("PING");
  const [buttonColor, setButtonColor] = useState("blue");
  const [isDisabled, setIsDisabled] = useState(false);

  const handleClick = async () => {
    setButtonText("PING...");
    setButtonColor("blue");
    setIsDisabled(true);

    try {
      const timeout = setTimeout(() => {
        // Если прошло 30 секунд и нет ответа, меняем на ошибку
        setButtonText("ERROR");
        setButtonColor("red");
        setTimeout(() => {
          setButtonText("PING");
          setButtonColor("blue");
          setIsDisabled(false);
        }, 3000); // Оставляем ошибку на 3 секунды
      }, 30000); // 30 секунд

      const response = await fetch("/api/v1/file/ping");
      clearTimeout(timeout); // Если ответ получен до 30 секунд, сбрасываем таймаут

      if (response.ok) {
        setButtonText("PONG");
        setButtonColor("green");
        // Через 3 секунды вернуть кнопку в исходное состояние
        setTimeout(() => {
          setButtonText("PING");
          setButtonColor("blue");
          setIsDisabled(false);
        }, 3000);
      } else {
        throw new Error("Ping failed");
      }
    } catch (error) {
      console.error("Ping request failed:", error);
      setButtonText("ERROR");
      setButtonColor("red");
      setTimeout(() => {
        setButtonText("PING");
        setButtonColor("blue");
        setIsDisabled(false);
      }, 3000); // Оставляем ошибку на 3 секунды
    }
  };

  return (
    <button
      onClick={handleClick}
      style={{
        backgroundColor: buttonColor,
        color: "white",
        padding: "10px 20px",
        border: "none",
        borderRadius: "5px",
        cursor: "pointer",
      }}
      disabled={isDisabled}
    >
      {buttonText}
    </button>
  );
}

export default PingButton;
