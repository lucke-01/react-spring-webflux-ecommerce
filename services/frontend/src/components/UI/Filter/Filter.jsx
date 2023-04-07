import { useState } from "react";
import { FILTER_ACTIVE_VALUES } from "utils/filter.utils";

import "./Filter.scss";
import Input from "../Input/Input";
import Select from "../Select/Select";

const Filter = ({
  showSearchBar = false,
  showActiveFilter = false,
  onChangeSearch = () => {},
  onChangeSelect = () => {},
}) => {
  const [selectValue, setSelectValue] = useState(true);

  const handleOnChangeSelect = (e) => {
    const value = e.target.value;
    setSelectValue(value);
    onChangeSelect(value);
  };

  return (
    <div className="filter-component">
      {showSearchBar && <Input label="Search" onChange={onChangeSearch} />}
      {showActiveFilter && (
        <Select
          label="Active"
          data={FILTER_ACTIVE_VALUES}
          value={selectValue}
          fullWidth={false}
          className="filter-component__select"
          onChange={handleOnChangeSelect}
        />
      )}
    </div>
  );
};

export default Filter;
