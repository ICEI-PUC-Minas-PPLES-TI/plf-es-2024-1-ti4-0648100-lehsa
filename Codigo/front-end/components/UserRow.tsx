"use client";
import React, { useState, useEffect } from "react";

interface User {
  id: number;
  nome: string;
  email: string;
  telefone: number;
  cpf: string;
  perfil_usuario: number;
}

function UserRow() {
  const [users, setUsers] = useState<User[]>([]);
  const [token, setToken] = useState<string>("");

  useEffect(() => {
    const authToken =
      "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJ1c3VhcmlvMDNAZXhhbXBsZS5jb20iLCJyb2xlIjoiYWRtaW4iLCJleHAiOjE3MTI5MDc3NDR9._Cc-50MEOLi9vIFk2lNxS0hhL6QujjOQVyWpYqeCewwGSF9VuK2GOYMN74nYrP_GmqcPqXws6eaUnFMm4vk0Mw";
    setToken(authToken);

    fetch(`http://localhost:8080/usuario`, {
      method: "GET",
      headers: {
        Authorization: `Bearer ${authToken}`,
        "Content-Type": "application/json",
      },
    })
      .then((response) => response.json())
      .then((data: User[]) => {
        setUsers(data);
      })
      .catch((error) => {
        console.error("Error fetching users:", error);
      });
  }, []);

  const renderProfileType = (perfil_usuario: number) => {
    if (perfil_usuario === 1) {
      return "ADM";
    } else if (perfil_usuario === 2) {
      return "Usuário";
    } else {
      return "Erro";
    }
  };

  const deleteUser = (id: number) => {
    fetch(`http://localhost:8080/usuario/${id}`, {
      method: "DELETE",
      headers: {
        Authorization: `Bearer ${token}`,
        "Content-Type": "application/json",
      },
    })
      .then(() => {
        setUsers(users.filter((user) => user.id !== id));
      })
      .catch((error) => {
        console.error("Error deleting user:", error);
      });
  };

  return (
    <div className="mt-10 flex justify-center">
      <div className="table w-[80%] rounded-xl bg-white">
        <div className="table-header-group">
          <div className="table-row h-[2rem]">
            <div className="table-cell text-left pl-5">Nome</div>
            <div className="table-cell text-left pl-5">Email</div>
            <div className="table-cell text-left pl-5">Celular</div>
            <div className="table-cell text-left pl-5">CPF</div>
            <div className="table-cell text-left pl-5">Tipo de perfil</div>
            <div className="table-cell text-left pl-5">Ação</div>
          </div>
        </div>

        {users.map((user, index) => (
          <ul
            className={`usersTable table-row-group ${
              index % 2 === 0 ? "evenRow" : "oddRow"
            }`}
            data-id={user.id}
            key={user.id}
          >
            <li className="table-cell text-left h-[3rem] align-middle pl-5">
              <p>{user.nome}</p>
            </li>
            <li className="table-cell text-left h-[3rem] align-middle pl-5">
              <p>{user.email}</p>
            </li>
            <li className="table-cell text-left h-[3rem] align-middle pl-5">
              <p>{user.telefone}</p>
            </li>
            <li className="table-cell text-left h-[3rem] align-middle pl-5">
              <p>{user.cpf}</p>
            </li>
            <li className="table-cell text-left h-[3rem] align-middle pl-5">
              <p>{renderProfileType(user.perfil_usuario)}</p>
            </li>
            <li className="table-cell text-left h-[3rem] align-middle pl-5 hover:text-red-500">
              <button onClick={() => deleteUser(user.id)}>Deletar</button>
            </li>
          </ul>
        ))}
      </div>
    </div>
  );
}

export default UserRow;
