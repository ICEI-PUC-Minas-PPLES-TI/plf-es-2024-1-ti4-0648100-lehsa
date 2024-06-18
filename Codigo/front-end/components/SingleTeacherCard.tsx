import { Separator } from "@/components/ui/separator";
import { EditIcon, TrashIcon } from "lucide-react";
import ImageComp from './ImageComp'
import {
  AlertDialog,
  AlertDialogAction,
  AlertDialogCancel,
  AlertDialogContent,
  AlertDialogDescription,
  AlertDialogFooter,
  AlertDialogHeader,
  AlertDialogTitle,
  AlertDialogTrigger,
} from "@/components/ui/alert-dialog";
import { UpdateTeacherDialog } from "./UpdateTeacherDialog";
import { showSuccessMessage } from "@/utils/toast";
import { deleteTeacherData } from "@/api/professor";

interface Props {
  id: string;
  nome: string;
  email: string;
  matricula: string;
  laboratorio: string;
  campus: string;
  lotacao: string;
  area_atuacao: string;
  onDelete: (id: string) => void;
}

const SingleTeacherCard = ({
  id,
  nome,
  email,
  matricula,
  laboratorio,
  campus,
  lotacao,
  area_atuacao,
  onDelete,
}: Props) => {
  const handleDelete = async () => {
    const deleted = await deleteTeacherData(id);
    if (deleted) {
      onDelete(id);
      showSuccessMessage("Professor deletado!");
    }
  };

  return (
    <section
      key={id}
      className="container mx-auto px-4 py-6 sm:py-8 md:px-6 lg:py-10 xl:py-12"
    >
      <div className="max-w-md mx-auto md:max-w-lg lg:max-w-2xl">
        <div className="rounded-lg bg-white p-6 shadow-md dark:bg-gray-950 hover:shadow-lg transition-shadow duration-300 ease-in-out">
          <div className="flex flex-col sm:flex-row items-center space-y-4 sm:space-y-0 sm:space-x-4">
            <ImageComp
              alt="foto do professor"
              className="h-16 w-16 rounded-full object-cover bg-slate-200"
              height={64}
              src={`${process.env.NEXT_PUBLIC_API_URL}/professor/img/${id}`}
              width={64}
            />
            <div className="text-center sm:text-left">
              <h3 className="text-lg font-semibold text-gray-900 dark:text-white">
                {nome}
              </h3>
              <p className="text-gray-600 dark:text-gray-400">{area_atuacao}</p>
            </div>
          </div>
          <Separator className="mt-3" />
          <ul className="mt-4 text-md leading-relaxed">
            <li className="flex flex-wrap gap-2">
              <h4 className="font-semibold text-gray-800 dark:text-gray-300">
                Matrícula:
              </h4>
              <p className="text-gray-600 dark:text-gray-400">{matricula}</p>
            </li>
            <li className="flex flex-wrap gap-2">
              <h4 className="font-semibold text-gray-800 dark:text-gray-300">
                Campus:
              </h4>
              <p className="text-gray-600 dark:text-gray-400">{campus}</p>
            </li>
            <li className="flex flex-wrap gap-2">
              <h4 className="font-semibold text-gray-800 dark:text-gray-300">
                Lotação:
              </h4>
              <p className="text-gray-600 dark:text-gray-400">{lotacao}</p>
            </li>
            <li className="flex flex-wrap gap-2">
              <h4 className="font-semibold text-gray-800 dark:text-gray-300">
                Laboratório:
              </h4>
              <p className="text-gray-600 dark:text-gray-400">{laboratorio}</p>
            </li>
            <li className="flex flex-wrap gap-2">
              <h4 className="font-semibold text-gray-800 dark:text-gray-300">
                E-mail:
              </h4>
              <p className="text-gray-600 dark:text-gray-400">{email}</p>
            </li>
          </ul>
          <div className="flex justify-end mt-4 space-x-2">
            <UpdateTeacherDialog id={id}>
            <button className="px-2 py-2 bg-yellow-500 text-white hover:bg-yellow-600 transition-colors flex items-center rounded-full">
              <EditIcon />
            </button>
            </UpdateTeacherDialog>
            <AlertDialog>
              <AlertDialogTrigger className="p-2 bg-red-600 rounded-full text-white hover:bg-red-700 transition duration-150 ease-in-out">
                <TrashIcon className="h-5 w-5" />
              </AlertDialogTrigger>
              <AlertDialogContent>
                <AlertDialogHeader>
                  <AlertDialogTitle>Tem certeza?</AlertDialogTitle>
                  <AlertDialogDescription>
                    Esta ação não pode ser revertida. Este professor será
                    deletado permanentemente do sistema.
                  </AlertDialogDescription>
                </AlertDialogHeader>
                <AlertDialogFooter>
                  <AlertDialogCancel className="border-none">
                    Cancelar
                  </AlertDialogCancel>
                  <AlertDialogAction
                    onClick={handleDelete}
                    className="bg-red-600 hover:bg-red-700 transition duration-150 ease-in-out"
                  >
                    Continuar
                  </AlertDialogAction>
                </AlertDialogFooter>
              </AlertDialogContent>
            </AlertDialog>
          </div>
        </div>
      </div>
    </section>
  );
};

export default SingleTeacherCard;
