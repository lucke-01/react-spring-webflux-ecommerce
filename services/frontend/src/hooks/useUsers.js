import { useContext, useEffect, useState } from "react";
import axios from "axios";
import toast from "react-hot-toast";
import { AuthContext } from "context";
import { BASE_API_URL, errorControl } from "utils";

const useUsers = (setError) => {
  const { token } = useContext(AuthContext);

  const [state, setState] = useState({
    users: [],
    active: [],
    isLoading: false,
  });

  const config = {
    headers: {
      Authorization: `Bearer ${token}`,
    },
  };

  const url = `${BASE_API_URL}/administrator`;

  const getUsers = async () => {
    setState({ ...state, isLoading: true });

    await axios
      .get(url, config)
      .then((resp) => {
        setState({
          ...state,
          users: resp.data,
          isLoading: false,
        });
      })
      .catch((err) => {
        toast.error("Something went wrong, Try it again later.", {
          position: "top-center",
        });
      });
  };

  const setActive = (id) => {
    setState({
      ...state,
      active: state.users.find((user) => user.id === id),
    });
  };

  const unsetActive = () => {
    setState({
      ...state,
      active: [],
    });
  };

  const createUser = async (user, closeModal) => {
    return await axios
      .post(url, user, config)
      .then((resp) => {
        toast.success("User created", {
          position: "top-center",
        });
        getUsers();
        closeModal();
      })
      .catch((err) => {
        setError(errorControl(err.response.data));
      });
  };

  const editUser = async (user, closeModal) => {
    return await axios
      .patch(url, user, config)
      .then((resp) => {
        toast.success("User edited", {
          position: "top-center",
        });
        getUsers();
        closeModal();
      })
      .catch((err) => {
        setError(errorControl(err.response.data));
      });
  };

  const deleteUser = async () => {
    await axios
      .delete(`${url}/${state.active.id}`, config)
      .then((resp) => {
        toast.success("User deleted", {
          position: "top-center",
        });
        getUsers();
        unsetActive();
      })
      .catch((err) => {
        toast.success("Something went wrong, Try it again later.", {
          position: "bottom-right",
        });
      });
  };

  useEffect(() => {
    getUsers();
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, []);

  return { ...state, createUser, setActive, unsetActive, deleteUser, editUser };
};

export default useUsers;
