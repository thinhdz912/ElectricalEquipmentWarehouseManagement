import React from "react";
import { Routes, Route, Navigate } from "react-router-dom";
import LoginPage from "./features/auth/login/LoginPage";

const App = () => {
  return (
    <Routes>
      <Route path="/login" element={<LoginPage />} />
      <Route path="*" element={<Navigate to="/login" replace />} />
    </Routes>
  );
};

export default App;
