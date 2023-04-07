import { useContext } from "react";
import axios from "axios";
import {AuthContext} from "context";
import {BASE_API_URL} from "utils";

export const useOrganisationService = () => {
    const { token } = useContext(AuthContext);
    const config = {
        headers: {
            Authorization: `Bearer ${token}`,
        }
    };
    const getOrganisationsSelectList = async () => {
      const url = `${BASE_API_URL}/organisation`;
    
      const data = await axios
        .get(url, config)
        .then((resp) => {
          const filteredOrganisations = resp?.data;
          const organisationsList = filteredOrganisations.map((org) => ({
            value: org.idOrganisation,
            label: org.name,
          }));
    
          return organisationsList;
        })
        .catch((err) => {});
    
      return data;
    };

    return {
      getOrganisationsSelectList
    };
}