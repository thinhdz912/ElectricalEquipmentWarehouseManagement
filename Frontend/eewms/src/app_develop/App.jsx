import { BrowserRouter as Router } from "react-router-dom";
import AppRoutes from "./routes/AppRoutes";
import { MaterialTailwindControllerProvider } from "./context";
import MainLayout from "./layouts/MainLayout";
import { AuthProvider } from "./context/AuthContext";

const App = () => {
  return (
    <AuthProvider>
      <MaterialTailwindControllerProvider>
        <Router>
          <MainLayout>
            <AppRoutes />
          </MainLayout>
        </Router>
      </MaterialTailwindControllerProvider>
    </AuthProvider>
  );
};

export default App;
