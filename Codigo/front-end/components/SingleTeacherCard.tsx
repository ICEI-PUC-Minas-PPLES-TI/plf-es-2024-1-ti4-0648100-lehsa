import { Separator } from "@/components/ui/separator";
import { EditIcon, TrashIcon } from "lucide-react";
import Image from "next/image";

interface Props {
    nome: string;
    email: string;
    matricula: string;
    laboratorio: string;
    campus: string;
    lotacao: string;
    area_atuacao: string;
  }

const SingleTeacherCard = ({ nome, email, matricula, laboratorio, campus, lotacao, area_atuacao }: Props) => {
  return (
    <section key="1" className="container mx-auto px-4 py-6 sm:py-8 md:px-6 lg:py-10 xl:py-12">
      <div className="max-w-md mx-auto md:max-w-lg lg:max-w-2xl">
        <div className="rounded-lg bg-white p-6 shadow-md dark:bg-gray-950 hover:shadow-lg transition-shadow duration-300 ease-in-out">
          <div className="flex flex-col sm:flex-row items-center space-y-4 sm:space-y-0 sm:space-x-4">
            <Image
              alt="Teacher 1"
              className="h-16 w-16 rounded-full object-cover bg-slate-200"
              height={64}
              src="../placeholder.svg"
              style={{
                aspectRatio: "1/1",
                objectFit: "cover",
              }}
              width={64}
            />
            <div className="text-center sm:text-left">
              <h3 className="text-lg font-semibold text-gray-900 dark:text-white">{nome}</h3>
              <p className="text-gray-600 dark:text-gray-400">{area_atuacao}</p>
            </div>
          </div>
          <Separator className="mt-3"/>
          <ul className="mt-4 text-md leading-relaxed">
            <li className="flex flex-wrap gap-2">
              <h4 className="font-semibold text-gray-800 dark:text-gray-300">Matrícula:</h4>
              <p className="text-gray-600 dark:text-gray-400">{matricula}</p>
            </li>
            <li className="flex flex-wrap gap-2">
              <h4 className="font-semibold text-gray-800 dark:text-gray-300">Campus:</h4>
              <p className="text-gray-600 dark:text-gray-400">{campus}</p>
            </li>
            <li className="flex flex-wrap gap-2">
              <h4 className="font-semibold text-gray-800 dark:text-gray-300">Lotação:</h4>
              <p className="text-gray-600 dark:text-gray-400">{lotacao}</p>
            </li>
            <li className="flex flex-wrap gap-2">
              <h4 className="font-semibold text-gray-800 dark:text-gray-300">Laboratório:</h4>
              <p className="text-gray-600 dark:text-gray-400">{laboratorio}</p>
            </li>
            <li className="flex flex-wrap gap-2">
              <h4 className="font-semibold text-gray-800 dark:text-gray-300">E-mail:</h4>
              <p className="text-gray-600 dark:text-gray-400">{email}</p>
            </li>
          </ul>
          <div className="flex justify-end mt-4 space-x-2">
            <button className="px-2 py-2 bg-yellow-500 text-white hover:bg-yellow-600 transition-colors flex items-center rounded-full">
              <EditIcon />
            </button>
            <button className="px-2 py-2 bg-red-500 text-white rounded-full hover:bg-red-700 transition-colors flex items-center">
              <TrashIcon />
            </button>
          </div>
        </div>
      </div>
    </section>
  );
};

export default SingleTeacherCard;
