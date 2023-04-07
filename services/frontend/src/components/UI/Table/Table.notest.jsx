// import { screen, render } from "@testing-library/react";
// import { AppButton, Table } from "components";
// import { LM_COLUMNS } from "utils";

// const ROWS = [
//   {
//     employee_id: "1",
//     application_id: "string",
//     name: "Rick Jones",
//     job_role: "Analyst",
//     app_role: "agl it",
//     access_level: "Advance",
//     review_date: null,
//     solved: false,
//     issues: <AppButton text="Show issues" onClick={jest.fn()} />,
//   },
// ];

// test("The table shows the info correctly", () => {
//   render(<Table columns={LM_COLUMNS} rows={ROWS} />);

//   //Testing that th are showing
//   const tableHead = screen.getByText(/line manager/i);
//   expect(tableHead).toBeInTheDocument();

//   //Testing that tr are showing
//   const tableRow = screen.getByText(ROWS[0].name);
//   expect(tableRow).toBeInTheDocument();

//   //Testing that the button is showing up
//   const button = screen.getByRole("button", { name: /show issues/i });
//   expect(button).toBeInTheDocument();
// });
