/* eslint-disable no-unused-vars */
import {
  TableCellsIcon,
  ServerStackIcon,
  Bars3BottomRightIcon,
} from "@heroicons/react/24/solid";

import {
  LockPerson,
  AccountCircle,
  Home
} from '@mui/icons-material';

import { Navigate } from "react-router-dom";

import UnauthorizedPage from "@/components/UnauthorizedPage";
import NotFoundPage from "@/components/NotFoundPage";
import LoginPage from "../features/auth/login/LoginPage";
import HomePage from "../features/home/HomePage";
import BranchManagement from "app/features/manager/4";

const icon = { className: "w-5 h-5 text-inherit" };

export const routes = [
  {
    title: "Default",
    layout: "default",
    pages: [
      {
        path: "/",
        element: <Navigate to="/login" replace />,
      },
      {
        path: "/branch-management",
        element: <BranchManagement />,
      },
    ],
  },
  {
    title: "",
    layout: "user",
    pages: [
      {
        icon: <Home {...icon} />,
        name: "Trang chủ",
        path: "/user/home",
        element: <HomePage />,
        roles: ["USER"],
      },
      
    ],
  },
  {
    title: "Auth Pages",
    layout: "auth",
    pages: [
      {
        icon: <ServerStackIcon {...icon} />,
        name: "Sign In",
        path: "/login",
        element: <LoginPage />,
      },
    ],
  },
  {
    title: "Other",
    layout: "other",
    pages: [
      {
        path: "/unauthorized",
        element: <UnauthorizedPage />,
      },
      {
        path: "*",
        element: <NotFoundPage />,
      },
    ],
  },
];

export default routes;