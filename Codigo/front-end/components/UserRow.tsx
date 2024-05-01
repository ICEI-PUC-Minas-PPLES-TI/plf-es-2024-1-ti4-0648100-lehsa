"use client";
import Cookie from "js-cookie";
import React, { useState, useEffect } from "react";
import { TrashIcon } from "lucide-react";

interface User {
  id: number;
  nome: string;
  email: string;
  telefone: number;
  cpf: string;
  perfil_usuario: number;
}

function UserRow({ searchTerm }: { searchTerm: string }) {
  const [users, setUsers] = useState<User[]>([]);
  const [token, setToken] = useState<string>("");
  const [loggedInUserId, setLoggedInUserId] = useState<number>(0); // State to store logged-in user ID

  useEffect(() => {
    const authToken = "Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJ1c3VhcmlvMDFAZXhhbXBsZS5jb20iLCJyb2xlIjoiYWRtaW4iLCJ1c2VySWQiOiJjOTI4NzdhNC1lYTZmLTRjYjctYmI5NC01NGVhYmEzZWVhYTgiLCJleHAiOjE3MTQ1Mzc5OTN9.N-lAIJ6LQZvZ0gODBcLQgGOIZ5u5Ns2DAogD6LeHT-mP7y28WfYBBn801dwBLls0l3tm-hlBJJyoO_iQAWFLsg";
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

    // Decode token to get the ID of the logged-in user
    try {
      const decodedToken = JSON.parse(atob(authToken.split(".")[1]));
      if (decodedToken && decodedToken.userId) {
        setLoggedInUserId(decodedToken.userId);
      }
    } catch (error) {
      console.error("Error decoding token:", error);
    }
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
    const confirmDelete = window.confirm("Quer mesmo deletar este usuário?");
    if (confirmDelete) {
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
    }
  };

  const filteredUsers = users.filter((user) =>
    user.nome.toLowerCase().includes(searchTerm.toLowerCase())
  );

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
            <div className="table-cell text-left pl-5"></div>
          </div>
        </div>

        {filteredUsers.map((user, index) => (
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
            <li className="table-cell text-left h-[3rem] align-middle pl-5 ">
              {loggedInUserId !== user.id && ( // Check if the user is not the logged-in user
                <button
                  className="transition ease-out hover:text-red-500 rounded-xl px-2"
                  onClick={() => deleteUser(user.id)}
                >
                  <TrashIcon className="h-4 w-4"/>
                </button>
              )}
            </li>
          </ul>
        ))}
      </div>
    </div>
  );
}

export default UserRow;
