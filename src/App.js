import { useEffect } from "react";
import { BrowserRouter, Routes, Route } from "react-router-dom";
import Chat from "./components/Chat";
import ProtectedRoute from "./components/ProtectedRoute";

export default function App() {

  useEffect(() => {
    // Временный тестовый токен
    localStorage.setItem("token", "test-token");
  }, []);

  return (
    <BrowserRouter>
      <Routes>
        <Route
          path="/chat"
          element={
            <ProtectedRoute>
              <Chat />
            </ProtectedRoute>
          }
        />
      </Routes>
    </BrowserRouter>
  );
}

