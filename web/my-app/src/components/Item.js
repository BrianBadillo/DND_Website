import React from 'react';
import './Item.css';

const Item = ({ item }) => {
    return (
        <div className="item-container">
            <p className="item-title">{item.item_name}</p>
            <p className="item-description">{item.item_description}</p>
            <p className="item-rarity">Rarity: {item.rarity}</p>
            <p className="item-type">Type: {item.type}</p>
        </div>
    );
};

export default Item;
