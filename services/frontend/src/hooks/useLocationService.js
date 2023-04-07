import { useContext } from "react";
import axios from "axios";
import {AuthContext} from "context";
import {BASE_API_URL} from "utils";

export const useLocationService = () => {
    const { token } = useContext(AuthContext);
    const config = {
        headers: {
            Authorization: `Bearer ${token}`,
        }
    };
    const getLocationsSelectList = async () => {
        const url = `${BASE_API_URL}/location`;
        return await axios
            .get(url, config)
            .then((resp) => {
              const filteredLocations = resp?.data;
      
              return filteredLocations.map((org) => ({
                value: org.id,
                label: org.name,
                reference: org.ref,
              }));
            })
            .catch((err) => {
            });
    };
    const getAllLocation = async () => {
      const url = `${BASE_API_URL}/location`;
      return await axios.get(url, config).then(data => data.data);
    }

    return {
        getLocationsSelectList, getAllLocation
    };
}