import { useCallback, useEffect, useState } from "react";
import { AppContext } from "./AppContext.js";
import { AppConstants } from "../utils/constants";
import { toast } from "react-toastify";
import { api } from "../utils/api.js";

export const AppContextProvider = (props) => {
  const backendURL = AppConstants.BACKEND_URL;
  const [isLoggedIn, setIsLoggedIn] = useState(false);
  const [userData, setUserData] = useState(false);

  const getUserData = useCallback(async () => {
    try {
      const response = await api.get(backendURL + "/profile");
      if (response.status === 200) {
        setUserData(response.data);
      } else {
        toast.error("Unable to retrieve profile");
      }
    } catch (error) {
      toast.error(error.message);
    }
  }, [backendURL]);

  useEffect(() => {
    let active = true;

    api
      .get(`${backendURL}/is-authenticated`)
      .then(async (response) => {
        if (!active) return;

        const authenticated = response.data === true;
        setIsLoggedIn(authenticated);

        if (authenticated) {
          await getUserData();
        } else {
          setUserData(false);
        }
      })
      .catch((error) => {
        if (!active) return;

        setIsLoggedIn(false);
        setUserData(false);

        // A 401 means the visitor is logged out, which is normal on first load.
        if (error.response?.status !== 401) {
          toast.error(error.response?.data?.message || error.message);
        }
      });

    return () => {
      active = false;
    };
  }, [backendURL, getUserData]);

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
