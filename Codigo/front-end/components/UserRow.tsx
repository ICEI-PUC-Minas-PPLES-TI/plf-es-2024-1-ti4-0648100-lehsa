import React from "react";

function createData(
  id: number,
  name: string,
  email: string,
  phone: number,
  company: string
) {
  return { id, name, email, phone, company };
}

const users = [
  createData(1, "User 1", "user1@email.com", 11111111, "instituicao 1"),
  createData(2, "User 2", "user1@email.com", 22222222, "instituicao 2"),
  createData(3, "User 3", "user1@email.com", 33333333, "instituicao 3"),
  createData(4, "User 4", "user1@email.com", 44444444, "instituicao 4"),
  createData(5, "User 5", "user1@email.com", 55555555, "instituicao 5"),
  createData(6, "User 6", "user1@email.com", 66666666, "instituicao 6"),
];

const UserRow = () => {
  return (
    <div className="mt-10 ml-72 table w-[80%] justify-center rounded-xl bg-white ">
      <div className="table-header-group">
        <div className="table-row h-[2rem]">
          <div className="table-cell text-left pl-5">Nome</div>
          <div className="table-cell text-left pl-5">Email</div>
          <div className="table-cell text-left pl-5">Celular</div>
          <div className="table-cell text-left pl-5">Instituicao</div>
        </div>
      </div>

      {users.map((user, index) => (
        <ul
          className={`usersTable table-row-group ${index % 2 === 0 ? 'evenRow' : 'oddRow'}`}
          data-id={user.id}
          key={user.id}
          // onClick={handleClick}
        >
          {Object.values(user).slice(1).map((value, index) => (
            <li
              className="table-cell text-left h-[3rem] align-middle pl-5"
              key={index}
              style={{ width: index === 0 ? '25%' : '25%' }} // Adjust width as needed
            >
              <p>{value}</p>
            </li>
          ))}
        </ul>
      ))}
    </div>
  );
};

export default UserRow;
