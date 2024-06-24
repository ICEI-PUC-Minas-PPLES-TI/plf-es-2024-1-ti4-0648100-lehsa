import React from "react";
import {Input} from "@/components/ui/input";
import {SearchIcon} from "lucide-react";

interface SearchBarProps {
    onChange: React.ChangeEventHandler<HTMLInputElement>; // Tipagem para onChange
}
const SearchBar = ({ onChange }: SearchBarProps) => {
    return (
        <div className='flex items-center '>
            <SearchIcon className='m-2 text-primary'/>
            <Input className='rounded-full w-56 max-w-lg text-primary caret-primary bg-white' placeholder="Buscar" type="Search" onChange={onChange}/>
        </div>
    )
}

export default SearchBar