/* eslint-disable no-unused-vars */
import React from "react";
import { Route, Routes } from "react-router-dom";
import routes from "./routes";
import { useAuth } from "@/context/AuthContext";
import UnauthorizedPage from "@/components/UnauthorizedPage";
import BranchManagement from "@/features/manager/4/BranchManagement";
import NotFoundPage from "@/components/NotFoundPage";
const AppRoutes = () => {
  return (
    <Routes>
      <Route path="/branch-management" element={<BranchManagement />} />
      {/* Các tuyến đường khác */}
    </Routes>
  );
};

export default AppRoutes;
