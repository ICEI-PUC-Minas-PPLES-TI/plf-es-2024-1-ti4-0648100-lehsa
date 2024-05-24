'use client'
import Cookie from 'js-cookie'
import { useEffect, useState } from "react";
import SingleTeacherCard from './SingleTeacherCard';


type Props = {
    id: string;
    nome: string;
    email: string;
    matricula: string;
    laboratorio: string;
    campus: string;
    lotacao: string;
    area_atuacao: string;
  }


const TeacherCard = () => {

    const [teacher, setTeacher] = useState<Props[]>([]);
    const token = Cookie.get("token");

    useEffect(() => {
        fetch("http://localhost:8080/professor", {
            method: 'GET',
            headers: {
                'Content-Type': 'application/json',
                "Authorization": `Bearer ${token}`
            }
        })
            .then(response => response.json())
            .then(data => {
                setTeacher(data)
            })
            .catch(error => console.error('Error fetching items:', error));
    }, []);

    return (
        <div className="grid grid-cols-1 gap-6 sm:grid-cols-2 md:grid-cols-3 lg:grid-cols-4">
            {teacher.map((teacher: Props) => (
                <div key={teacher.id}>
                    <SingleTeacherCard nome={teacher.nome} email={teacher.email} matricula={teacher.matricula} laboratorio={teacher.laboratorio} campus={teacher.campus} lotacao={teacher.lotacao} area_atuacao={teacher.area_atuacao}/>
                    </div>
            ))}
        </div>
    );
};

export default TeacherCard;