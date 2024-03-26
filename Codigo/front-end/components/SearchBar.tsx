import React from "react";
import {Input} from "@/components/ui/input";
import {SearchIcon} from "lucide-react";

const SearchBar = () => {
    return (
        <div className='flex items-center '>
            <SearchIcon className='m-2 text-primary'/>
            <Input className='rounded-full max-w-lg text-primary caret-primary' placeholder="Search" type="Search"/>
        </div>
    )
}

export default SearchBar