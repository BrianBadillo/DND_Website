import React, { useState } from "react";
import { mockData } from "../data"; // Assuming your data file is in src/
import Item from "./Item";

const DisplayCharacters = ({ userId }) => {
    const { characters, groups, items } = mockData;

    const [showSharedOnly, setShowSharedOnly] = useState(false); // State to toggle between "Shared Only" and "All Items"

    // Filter characters for the given userId
    const filteredCharacters = characters.filter(char => char.user_id === userId);

    // Helper function to get group name by group_id
    const getGroupName = groupId => {
        const group = groups.find(g => g.group_id === groupId);
        return group ? group.name : "No group";
    };

    // Filter items based on the toggle state
    const filteredItems = (charId) => {
        return items
            .filter(item => item.character_id === charId)
            .filter(item => (showSharedOnly ? item.is_shared : true)); // Show shared items or all items
    };

    return (
        <div>
            <h2>Characters for User ID {userId}</h2>
            
            {/* Toggle Switch */}
            <label>
                Show Shared Items Only
                <input 
                    type="checkbox" 
                    checked={showSharedOnly} 
                    onChange={() => setShowSharedOnly(!showSharedOnly)} 
                />
            </label>

            {filteredCharacters.length > 0 ? (
                <ul>
                    {filteredCharacters.map(char => (
                        <li key={char.char_id}>
                            <strong>{char.name}</strong> - Level {char.level} {char.class} <br />
                            Group: <em>{getGroupName(char.group_id)}</em>
                            <h4>Items:</h4>
                            {filteredItems(char.char_id).map(item => (
                                <Item key={item.item_id} item={item} />
                            ))}
                        </li>
                    ))}
                </ul>
            ) : (
                <p>No characters found for this user.</p>
            )}
        </div>
    );
};

export default DisplayCharacters;
