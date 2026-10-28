import { createContext, useContext, useEffect, useState } from "react";
import { AppConstants } from "../utils/constants";
import { toast } from "react-toastify";
import axios from "axios";

export const AppContext = createContext();

export const AppContextProvider = (props) => {
  axios.defaults.withCredentials = true;

  const backendURL = AppConstants.BACKEND_URL;
  const [isLoggedIn, setIsLoggedIn] = useState(false);
  const [userData, setUserData] = useState(false);

  const getUserData = async () => {
    try {
      const response = await axios.get(backendURL + "/profile");
      if (response.status === 200) {
        setUserData(response.data);
      } else {
        toast.error("Unable to retrieve profile");
      }
    } catch (error) {
      toast.error(error.message);
    }
  };

  const getAuthState = async () => {
    try {
      const response = await axios.get(`${backendURL}/is-authenticated`);
      const authenticated = response.data === true;

      setIsLoggedIn(authenticated);

      if (authenticated) {
        await getUserData();
      } else {
        setUserData(false);
      }
    } catch (error) {
      setIsLoggedIn(false);
      setUserData(false);

      // A 401 means the visitor is logged out, which is normal on first load.
      if (error.response?.status !== 401) {
        toast.error(error.response?.data?.message || error.message);
      }
    }
  };

  useEffect(() => {
    getAuthState();
  }, []);

  const contextValue = {
    backendURL,
    isLoggedIn,
    setIsLoggedIn,
    userData,
    setUserData,
    getUserData,
  };

  return (
    <AppContext.Provider value={contextValue}>
      {props.children}
    </AppContext.Provider>
  );
};
