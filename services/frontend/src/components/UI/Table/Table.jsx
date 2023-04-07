import React from "react";
import Box from "@mui/material/Box";
import MuiTable from "@mui/material/Table";
import TableBody from "@mui/material/TableBody";
import TableCell from "@mui/material/TableCell";
import TableContainer from "@mui/material/TableContainer";
import TableHead from "@mui/material/TableHead";
import TableRow from "@mui/material/TableRow";
import TableSortLabel from "@mui/material/TableSortLabel";
import { visuallyHidden } from "@mui/utils";
import { Tooltip } from "@mui/material";

import "./Table.scss";

function descendingComparator(a, b, orderBy) {
  if (b[orderBy] < a[orderBy]) {
    return -1;
  }
  if (b[orderBy] > a[orderBy]) {
    return 1;
  }
  return 0;
}

function getComparator(order, orderBy) {
  return order === "desc"
    ? (a, b) => descendingComparator(a, b, orderBy)
    : (a, b) => -descendingComparator(a, b, orderBy);
}

// Since 2020 all major browsers ensure sort stability with Array.prototype.sort().
// stableSort() brings sort stability to non-modern browsers (notably IE11). If you
// only support modern browsers you can replace stableSort(exampleArray, exampleComparator)
// with exampleArray.slice().sort(exampleComparator)
function stableSort(array, comparator) {
  const stabilizedThis = array.map((el, index) => [el, index]);
  stabilizedThis.sort((a, b) => {
    const order = comparator(a[0], b[0]);
    if (order !== 0) {
      return order;
    }
    return a[1] - b[1];
  });
  return stabilizedThis.map((el) => el[0]);
}

const EnhancedTableHead = ({
  columns,
  order,
  orderBy,
  onRequestSort,
  actions,
  superAdmin,
}) => {
  const createSortHandler = (property) => (event) => {
    onRequestSort(event, property);
  };

  const newActions = Object.keys(actions).filter((a) => a.includes("Action"));

  return (
    <TableHead>
      <TableRow>
        {columns.map((column) => {
          if (
            column.label.toLowerCase() === "actions" &&
            superAdmin === false &&
            newActions.includes("editAction") &&
            newActions.includes("deleteAction") &&
            newActions.length <= 2
          ) {
            return "";
          }

          return (
            <TableCell
              key={column.key}
              sortDirection={orderBy === column.key ? order : false}
              size="small"
            >
              <TableSortLabel
                active={orderBy === column.key}
                direction={orderBy === column.key ? order : "asc"}
                onClick={createSortHandler(column.key)}
              >
                {column.label}
                {orderBy === column.key ? (
                  <Box component="span" sx={visuallyHidden}>
                    {order === "desc"
                      ? "sorted descending"
                      : "sorted ascending"}
                  </Box>
                ) : null}
              </TableSortLabel>
            </TableCell>
          );
        })}
      </TableRow>
    </TableHead>
  );
};

const Table = ({
  columns = [],
  rows = [],
  actions = {},
  superAdmin = false,
  ...others
}) => {
  const [order, setOrder] = React.useState("asc");
  const [orderBy, setOrderBy] = React.useState("calories");

  const renderCell = (user, columnKey, actions) => {
    const cellValue = user[columnKey];
    const {
      editAction = false,
      onEdit = () => {},
      deleteAction = false,
      onDelete = () => {},
      locationAction = false,
      onLocation = () => {},
      manageAction = false,
      onManage = () => {},
      duplicateAction = false,
      onDuplicate = () => {},
    } = actions;

    switch (columnKey) {
      case "actions":
        return (
          <div className="actions-link">
            {locationAction && (
              <Tooltip title="Location" placement="top" arrow>
                <i
                  role="tooltip"
                  id={user.idTable}
                  className="bi bi-geo-alt edit-link"
                  onClick={() => onLocation(user.idTable, user?.name)}
                />
              </Tooltip>
            )}
            {manageAction && (
              <Tooltip title="Manage" placement="top" arrow>
                <i
                  role="tooltip"
                  id={user.idTable}
                  data-ref={user?.id}
                  className="bi bi-sliders edit-link"
                  onClick={(e) => onManage(e, user?.name)}
                />
              </Tooltip>
            )}
            {duplicateAction && superAdmin && (
              <Tooltip title="Duplicate" placement="top" arrow>
                <i
                  role="tooltip"
                  id={user.idTable}
                  className="bi bi-layers edit-link"
                  onClick={onDuplicate}
                />
              </Tooltip>
            )}
            {editAction && superAdmin && (
              <Tooltip title="Edit" placement="top" arrow>
                <i
                  role="tooltip"
                  id={user.idTable}
                  className="bi bi-pencil edit-link"
                  onClick={onEdit}
                />
              </Tooltip>
            )}
            {deleteAction && superAdmin && (
              <Tooltip title="Delete" placement="top" arrow>
                <i
                  role="tooltip"
                  id={user.idTable}
                  className="bi bi-trash3 delete-link"
                  onClick={onDelete}
                />
              </Tooltip>
            )}
          </div>
        );
      default:
        return cellValue;
    }
  };

  const handleRequestSort = (event, property) => {
    const isAsc = orderBy === property && order === "asc";
    setOrder(isAsc ? "desc" : "asc");
    setOrderBy(property);
  };

  const columnKey = columns.map((cell) => cell.key);

  return (
    <TableContainer {...others}>
      <MuiTable stickyHeader={true} sx={{ minWidth: 750 }}>
        <EnhancedTableHead
          actions={actions}
          superAdmin={superAdmin}
          columns={columns}
          order={order}
          orderBy={orderBy}
          onRequestSort={handleRequestSort}
        />
        <TableBody>
          {!rows.length ? (
            <TableRow role="row">
              <TableCell colSpan={columns.length}>No records found</TableCell>
            </TableRow>
          ) : (
            stableSort(rows, getComparator(order, orderBy)).map((row) => (
              <TableRow role="row" hover tabIndex={-1} key={row.idTable}>
                {columnKey.map((key) => (
                  <TableCell key={key} size="small">
                    {renderCell(row, key, actions)}
                  </TableCell>
                ))}
              </TableRow>
            ))
          )}
        </TableBody>
      </MuiTable>
    </TableContainer>
  );
};

export default Table;
