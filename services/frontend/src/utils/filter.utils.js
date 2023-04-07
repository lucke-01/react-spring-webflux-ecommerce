export const FILTER_ACTIVE_VALUES = [
    { value: "all", label: "All" },
    { value: true, label: "Active" },
    { value: false, label: "No Active" }
]

export const filterSearchText = (filterParams, textSearchFilter, rows) => {
    const params = filterParams.map(({ key }) => key);
    return rows.filter((row) => {
      const newObject = params.reduce((acc, element) => {
        if (element === "actions") {
          acc[element] = "";
        } else {
          acc[element] = row[element];
        }
        return acc;
      }, {});
      return Object.values(newObject)
        .join()
        .toLowerCase()
        .includes(textSearchFilter.toLowerCase());
    });
}
export const filterSelectFilter = (selectFilterValue, field, rows, allValue = 'all') => {
    let filteredRows = rows;
    if (selectFilterValue != null && selectFilterValue !== allValue) {
      filteredRows = rows.filter(row => row[field] === selectFilterValue);
    }
    return filteredRows;
};
export const filterActiveSelectFilter = (selectFilterValue, rows, allValue = 'all') => {
    let filteredRows = rows;
    if (selectFilterValue != null && selectFilterValue !== allValue) {
        filteredRows = rows.filter(p => {
            if (selectFilterValue === true) {
              return p.active === selectFilterValue;
            } else {
              return p.active == null || p.active === selectFilterValue;
            }
          });
    }
    return filteredRows;
};