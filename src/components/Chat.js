import { useState } from "react";
import axios from "axios";

export default function Chat() {

  const [message, setMessage] = useState("");
  const [chat, setChat] = useState([]);

  const sendMessage = async () => {
    if (!message.trim()) return;

    const token = localStorage.getItem("token");

    try {
      // добавляем сообщение пользователя в UI
      setChat(prev => [...prev, { from: "user", text: message }]);

      const response = await axios.post(
        "http://localhost:8080/api/chat",
        { message },
        {
          headers: {
            Authorization: `Bearer ${token}`,
            "Content-Type": "application/json"
          }
        }
      );

      // добавляем ответ AI
      setChat(prev => [...prev, { from: "ai", text: response.data.reply }]);

    } catch (err) {
      console.error(err);
      alert("Ошибка при отправке сообщения");
    }

    setMessage("");
  };

  return (
    <div style={{ maxWidth: 600, margin: "0 auto", padding: 20 }}>
      <h2>AI Chat</h2>

      <div style={{
        border: "1px solid #ccc",
        padding: 10,
        height: 300,
        overflowY: "auto",
        marginBottom: 20
      }}>
        {chat.map((msg, i) => (
          <div key={i} style={{
            textAlign: msg.from === "user" ? "right" : "left",
            margin: "8px 0"
          }}>
            <strong>{msg.from === "user" ? "You" : "AI"}:</strong> {msg.text}
          </div>
        ))}
      </div>

      <input
        type="text"
        value={message}
        onChange={e => setMessage(e.target.value)}
        placeholder="Type your message..."
        style={{ width: "80%", padding: 10 }}
      />
      <button onClick={sendMessage} style={{ padding: "10px 20px", marginLeft: 10 }}>
        Send
      </button>
    </div>
  );
}
