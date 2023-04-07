import PropTypes from 'prop-types';
import { useMemo, useState } from 'react';
import MaterialReactTable from 'material-react-table';
import Button from "../Button/Button";

const SearchSelector = ({ dataList, dataColumns, selectRow, multiSelection = false }) => {
    const [rowSelection, setRowSelection] = useState({});
    const columns = useMemo(() => dataColumns
        // eslint-disable-next-line react-hooks/exhaustive-deps
        ,[]);
    const selectValues = () => {
        let selection = Object.keys(rowSelection);
        if (selection.length > 0) {
            if (multiSelection === false) {
                selection = selection[0];
            }
            selectRow(selection);
        }
    };
    return (
        <div>
            <div>
                <MaterialReactTable 
                    columns={columns} 
                    data={dataList}
                    enableRowSelection
                    getRowId={(originalRow) => originalRow.id}
                    onRowSelectionChange={setRowSelection}
                    state={{ rowSelection }}
                    enableMultiRowSelection={multiSelection}
                    />
            </div>
            <div style={{marginTop: "0.75em"}} className="d-flex justify-content-center">
                <Button 
                        className={ Object.keys(rowSelection).length > 0 ? "" : "button-cancel" }
                        disabled={ Object.keys(rowSelection).length > 0 ? false : true }
                        text="Select"
                        onClick={selectValues} />
            </div>
        </div>
    );
};

SearchSelector.propTypes = {
    dataList: PropTypes.array.isRequired,
    dataColumns: PropTypes.array.isRequired,
    selectRow: PropTypes.func.isRequired,
    multiSelection: PropTypes.bool
};

export default SearchSelector;