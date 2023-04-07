import { useContext } from "react";
import {
  BrowserRouter as Router,
  Routes,
  Route,
  Navigate,
} from "react-router-dom";
import {Toaster} from "react-hot-toast";
import { AuthContext } from "./context";
import RestorePassword from "./pages/RestorePassword/RestorePassword";
import { ErrorBoundary } from "components/error/ErrorBoundary";
import "./app.scss";
import ForgotPassword from "./pages/ForgotPassword/ForgotPassword";
import Login from "./pages/Login/Login";
import Layout from "./components/Layout/Layout";

const ProtectedRoute = ({ isLogged, children }) => {
  if (!isLogged) {
    return <Navigate to="/login" />;
  }
  return children;
};

function App() {
  const { logged } = useContext(AuthContext);

  return (
    <>
    <Toaster
    position="top-center"
    reverseOrder={false}
    />
    <ErrorBoundary>
      <Router>
        <Routes>
          <Route
            path="/login"
            element={logged ? <Navigate to="/" /> : <Login />}
          />
          <Route
            path="/forgotpassword"
            element={logged ? <Navigate to="/" /> : <ForgotPassword />}
          />
          <Route
            path="/restore-password/:passwordToken"
            element={logged ? <Navigate to="/" /> : <RestorePassword />}
          />
          <Route
            path="/*"
            element={
              <ProtectedRoute isLogged={logged}>
                <Layout />
              </ProtectedRoute>
            }
          />
        </Routes>
      </Router>
    </ErrorBoundary>
    </>
  );
}
export default App;
