import { useContext, useEffect, useState } from "react";
import { useTranslation } from "react-i18next";
import { useFilter, useUsers } from "hooks";
import { AuthContext } from "context";

import Card from "../../components/UI/Card/Card";
import Table from "../../components/UI/Table/Table";
import Button from "../../components/UI/Button/Button";
import {
  allowActionBaseOnRole,
  normalizeUsers,
  screens,
  userActions,
  USER_LIST_COLUMNS,
} from "utils";

import "./users.scss";
import Loader from "../../components/UI/Loader/Loader";
import Filter from "../../components/UI/Filter/Filter";
import UserForm from "../../components/forms/UserForm/UserForm";
import Confirmation from "../../components/UI/Confirmation/Confirmation";
import Modal from "../../components/UI/Modal/Modal";

const Users = () => {
  const [normalizedUsers, setNormalizedUsers] = useState([]);
  const [showCreateModal, setShowCreateModal] = useState(false);
  const [showEditModal, setShowEditModal] = useState(false);
  const [showConfirmationModal, setShowConfirmationModal] = useState(false);
  const [error, setError] = useState("");

  const { role } = useContext(AuthContext);

  const {
    users,
    active,
    createUser,
    deleteUser,
    setActive,
    editUser,
    isLoading,
  } = useUsers(setError);

  const { filteredData, onChangeSearch } = useFilter({
    data: normalizedUsers,
    filterParams: USER_LIST_COLUMNS,
  });

  const { t } = useTranslation();

  useEffect(() => {
    if (users) {
      setNormalizedUsers(normalizeUsers(users));
    }
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, [users]);

  const onOpenCreateModal = () => {
    setError(null);
    setShowCreateModal(true);
  };

  const onCloseCreateModal = () => setShowCreateModal(false);

  const onOpenEditModal = (e) => {
    setError(null);
    setActive(e.target.id);
    setShowEditModal(true);
  };

  const onCloseEditModal = () => setShowEditModal(false);

  const manageConfirmationModal = () =>
    setShowConfirmationModal(!showConfirmationModal);

  const onShowConfirmation = (e) => {
    setActive(e.target.id);
    manageConfirmationModal();
  };

  const onCreateUser = (user) => createUser(user, onCloseCreateModal);

  const onEdit = (user) => editUser(user, onCloseEditModal);

  const onDelete = () => {
    deleteUser();
    manageConfirmationModal();
  };

  const actions = {
    editAction: allowActionBaseOnRole(screens.users, role, userActions.edit),
    onEdit: onOpenEditModal,
    deleteAction: allowActionBaseOnRole(
      screens.users,
      role,
      userActions.delete
    ),
    onDelete: onShowConfirmation,
  };

  return (
    <div className="users-screen screen__wrapper">
      <p className="users-screen__description">{t("users.description")}</p>
      <p className="users-screen__title">{t("users.title")}</p>
      <Card>
        <div className="users-screen__card--actions-bar">
          {allowActionBaseOnRole(screens.users, role, userActions.create) && (
            <Button text="Create User" handleOnClick={onOpenCreateModal} />
          )}

          <Filter showSearchBar={true} onChangeSearch={onChangeSearch} />
        </div>

        {isLoading ? (
          <Loader />
        ) : (
          <Table
            columns={USER_LIST_COLUMNS}
            superAdmin={true}
            rows={filteredData}
            actions={actions}
          />
        )}
      </Card>

      {showCreateModal && (
        <Modal
          title="Create User"
          visibility={showCreateModal}
          handleOnClose={onCloseCreateModal}
          alert={error}
        >
          <UserForm onClick={onCreateUser} isEdit={false} />
        </Modal>
      )}

      {showEditModal && (
        <Modal
          title="Edit User"
          visibility={showEditModal}
          handleOnClose={onCloseEditModal}
          alert={error}
        >
          <UserForm onClick={onEdit} initialState={active} isEdit={true} />
        </Modal>
      )}

      {showConfirmationModal && (
        <Confirmation
          visibility={showConfirmationModal}
          handleOnDelete={onDelete}
          handleOnClose={manageConfirmationModal}
        />
      )}
    </div>
  );
};

export default Users;
