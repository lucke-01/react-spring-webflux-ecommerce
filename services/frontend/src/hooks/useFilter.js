import { useEffect, useState } from "react";

const useFilter = ({ data, filterParams = ["name"] }) => {
  const [filteredData, setFilteredData] = useState([]);
  const [filters, setFilters] = useState({
    status: true,
    searchText: "",
  });

  useEffect(() => {
    setFilteredData(data);
    onFilter();
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, [data]);

  useEffect(() => {
    onFilter();
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, [filters]);

  const onChangeSearch = (e) =>
    setFilters({ ...filters, searchText: e.target.value.toLowerCase() });

  const onChangeSelect = (value) => {
    const newData = data.filter((item) =>
      value === "all" ? item : item.active === value ? item : false
    );
    setFilters({ ...filters, status: value });
    setFilteredData(newData);
  };

  const onFilter = () => {
    const params = filterParams
      .filter(({ key }) => key !== "actions")
      .map(({ key }) => key);

    const filteredRows = data.filter((row) => {
      const newString = params.reduce((acc, element) => {
        acc.push(row[element]);
        return acc;
      }, []);

      if (!Object.keys(row).includes("active")) {
        return newString
          .join()
          .toLowerCase()
          .includes(filters.searchText.toLowerCase());
      }

      return newString
        .join()
        .toLowerCase()
        .includes(filters.searchText.toLowerCase())
        ? filters.status === "all"
          ? true
          : row.active === filters.status
        : false;
    });

    setFilteredData(filteredRows);
  };

  return { filteredData, onChangeSearch, onChangeSelect };
};

export default useFilter;
