import { useEffect, useState } from "react";
import { BASE_API_URL } from "../utils";
import axios from "axios";

const useBillers = () => {
  const [state, setState] = useState({
    dataBillers: [],
    isLoading: true,
  });

  const url = `${BASE_API_URL}/biller`;

  const config = {
    headers: {
      Accept: "application/json, text/plain, */*",
      "Content-Type": "application/json",
    },
  };

  const getBillers = async (data) => {
    setState({
      ...state,
      isLoading: true,
    });
    let urlNice = `${url}?refsLocation=${data.refsLocation}&startTime=${data.startTime}&endTime=${data.endTime}&isMonthly=${data.isMonthly}`;
    if (data.active!=="all") {
      urlNice = urlNice + `&active=${data.active}`
    }

    await axios
      .get(urlNice, config)
      .then((resp) => {
        setState({
          ...state,
          dataBillers: resp.data[0].billers,
          isLoading: false,
          error: "",
        });
      })
      .catch(({ response }) => {
        setState({
          ...state,
          isLoading: false,
          error: response.data,
        });
      });
  };

  useEffect(() => {
    getBillers();
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, []);

  return {
    ...state,
    getBillers,
  };
};

export default useBillers;
