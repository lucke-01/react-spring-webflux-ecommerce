/* eslint-disable testing-library/no-node-access */
/* eslint-disable testing-library/no-container */
import { render, screen, cleanup, waitFor } from "@testing-library/react";
import userEvent from '@testing-library/user-event';
import SearchSelector from "./SearchSelector";

//MOCKS
const selectRow = jest.fn();
//END MOCKS
describe("Search selector tests",  () => {
    beforeEach(() => {cleanup();});

    test("should list table", async () => {
        //GIVEN
        const userActions = userEvent.setup();
        const dataList = [
            {
                id: "id1",
                name: "name1"
            },
            {
                id: "id2",
                name: "name2"
            }
        ];
        //columns
        const DATA_COLUMN_NAME = [{accessorKey: "name", header: "Name"}];

        //render component
        const { container } = render(
            <SearchSelector dataList={dataList} dataColumns={DATA_COLUMN_NAME} selectRow={selectRow}></SearchSelector>
        );
        //WHEN selector is loaded
        const selectButton = screen.getByRole('button', {
            name: /select/i
        });
        const rows = screen.getAllByRole('row');
        await waitFor(() => {
            expect(rows).toHaveLength(dataList.length +1);
        });
        //THEN
        expect(selectButton).toBeInTheDocument();
    });
    test("should select row and accept", async () => {
        //GIVEN
        const userActions = userEvent.setup();
        const dataList = [
            {
                id: "id1",
                name: "name1"
            },
            {
                id: "id2",
                name: "name2"
            }
        ];
        //columns
        const DATA_COLUMN_NAME = [{accessorKey: "name", header: "Name"}];

        //render component
        const { container } = render(
            <SearchSelector dataList={dataList} dataColumns={DATA_COLUMN_NAME} selectRow={selectRow}></SearchSelector>
        );
        
        const selectButton = screen.getByRole('button', {
            name: /select/i
        });
        //WHEN 
        const rows = screen.getAllByRole("radio");
        await userActions.click(rows[0]);
        await userActions.click(selectButton);
        console.log(selectRow.mock.calls);
        //THEN
        expect(selectRow).toBeCalled();
    });
    test("should not select row and accept", async () => {
        //GIVEN
        const userActions = userEvent.setup();
        const dataList = [
            {
                id: "id1",
                name: "name1"
            },
            {
                id: "id2",
                name: "name2"
            }
        ];
        //columns
        const DATA_COLUMN_NAME = [{accessorKey: "name", header: "Name"}];

        //render component
        const { container } = render(
            <SearchSelector dataList={dataList} dataColumns={DATA_COLUMN_NAME} selectRow={selectRow}></SearchSelector>
        );
        
        const selectButton = screen.getByRole('button', {
            name: /select/i
        });
        //WHEN 
        await userActions.click(selectButton);
        console.log(selectRow.mock.calls);
        //THEN
        expect(selectRow).not.toBeCalled();
    });
});