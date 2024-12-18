// SPDX-License-Identifier: MIT
// Mokin Anton
pragma solidity ^0.8.0;

contract SecurityExchange {
    // Структура для хранения информации о ценной бумаге
    struct Security {
        uint256 id;
        string name;
        uint256 price;
        address owner;
    }

    // Маппинг для хранения данных о ценных бумагах
    mapping(uint256 => Security) public securities;

    // Маппинг для хранения ценных бумаг, принадлежащих адресам владельцев
    mapping(address => uint256[]) private ownedSecurities;

    // Счетчик для генерации уникального ID
    uint256 public nextSecurityId = 1;

    // События
    event SecurityPurchased(address indexed buyer, uint256 securityId, uint256 price);
    event SecurityExchanged(address indexed from, address indexed to, uint256 securityId);

    // Mokin Anton
    // Функция для покупки ценной бумаги
    function purchaseSecurity(string memory name, uint256 price) public {
        uint256 securityId = nextSecurityId++;
        securities[securityId] = Security(securityId, name, price, msg.sender);
        ownedSecurities[msg.sender].push(securityId);

        emit SecurityPurchased(msg.sender, securityId, price);
    }

    // Mokin Anton
    // Функция для обмена ценной бумаги другому пользователю
    function exchangeSecurity(uint256 securityId, address to) public {
        require(securities[securityId].owner == msg.sender, "You do not own this security");
        securities[securityId].owner = to;
        removeSecurityFromOwner(msg.sender, securityId);
        ownedSecurities[to].push(securityId);
        emit SecurityExchanged(msg.sender, to, securityId);
    }

    // Mokin Anton
    // Вспомогательная функция для удаления ценной бумаги из списка владельца
    function removeSecurityFromOwner(address owner, uint256 securityId) private {
        uint256[] storage owned = ownedSecurities[owner];
        for (uint256 i = 0; i < owned.length; i++) {
            if (owned[i] == securityId) {
                owned[i] = owned[owned.length - 1];
                owned.pop();
                break;
            }
        }
    }

    // Mokin Anton
    // Функция для получения списка ценных бумаг владельца
    function getOwnedSecurities(address owner) public view returns (uint256[] memory) {
        return ownedSecurities[owner];
    }
}
