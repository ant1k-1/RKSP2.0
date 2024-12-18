// SPDX-License-Identifier: GPL-3.0
pragma solidity >=0.7.0 <0.9.0;

// Импорт вспомогательных файлов и контрактов
import "remix_tests.sol"; // Инструмент для тестирования в Remix
import "../contracts/Security.sol"; // Путь к вашему контракту SecurityExchange

// Контракт для тестирования функционала SecurityExchange
// Mokin Anton
contract SecurityExchangeTest {
    
    // Экземпляр контракта, который будет тестироваться
    SecurityExchange exchangeToTest;

    // Вспомогательные переменные для тестов
    address buyer = address(0x123);       // Адрес покупателя
    address seller = address(0x456);      // Адрес продавца
    address newOwner = address(0x789);    // Новый владелец
    uint256 securityId;                   // ID ценной бумаги

    // Выполняется перед всеми тестами
    function beforeAll() public {
        // Создаем новый экземпляр контракта SecurityExchange
        exchangeToTest = new SecurityExchange();
    }

    // Тестирование покупки ценной бумаги
    function checkPurchaseSecurity() public {
        // Покупка ценной бумаги с названием "Stock A" и ценой 100
        exchangeToTest.purchaseSecurity("Stock A", 100);

        // Получаем данные о купленной ценной бумаге с ID = 1
        (, string memory name, uint256 price, address owner) = exchangeToTest.securities(1);
        
        // Проверяем корректность данных
        Assert.equal(name, "Stock A", "Name should be 'Stock A'");
        Assert.equal(price, 100, "Price should be 100");
        Assert.equal(owner, buyer, "Owner should be the buyer address");

        // Присваиваем ID для дальнейших тестов
        securityId = 1;
    }

    // Mokin Anton
    // Тестирование обмена ценной бумаги новому владельцу
    function checkExchangeSecurity() public {
        // Покупка ценной бумаги с названием "Stock B" и ценой 200
        exchangeToTest.purchaseSecurity("Stock B", 200);
        
        // Обмен ценной бумаги с ID = 2 новому владельцу
        exchangeToTest.exchangeSecurity(2, newOwner);

        // Получаем данные о ценной бумаге после обмена
        (, , , address owner) = exchangeToTest.securities(2);
        
        // Проверяем, что владелец изменился на newOwner
        Assert.equal(owner, newOwner, "Owner should be the newOwner address");
    }

    // Mokin Anton
    // Тестирование запрета обмена бумагой без владения
    function checkCannotExchangeWithoutOwnership() public {
        // Покупка новой ценной бумаги с названием "Stock C" и ценой 300
        exchangeToTest.purchaseSecurity("Stock C", 300);

        // Попытка обмена бумаги, которой владеет другой адрес
        try exchangeToTest.exchangeSecurity(securityId, newOwner) {
            Assert.ok(false, "Non-owner should not be able to exchange security");
        } catch Error(string memory reason) {
            // Проверка, что выброшенная ошибка соответствует ожиданиям
            Assert.equal(
                reason, 
                "You do not own this security", 
                "Expected 'You do not own this security' error"
            );
        }
    }
}







// SPDX-License-Identifier: GPL-3.0
pragma solidity >=0.7.0 <0.9.0;

// Импорт контракта для тестирования
import "remix_tests.sol";
import "../contracts/Security.sol";

contract SecurityExchangeTest {
    // Экземпляр контракта для тестирования
    SecurityExchange exchangeToTest;

    // Вспомогательные адреса
    address buyer = address(0x123);       // Адрес покупателя
    address newOwner = address(0x789);    // Новый владелец
    address seller = address(this);       // Тестовый контракт как продавец
    uint256 securityId;                   // ID ценной бумаги

    // Выполняется перед всеми тестами
    function beforeAll() public {
        exchangeToTest = new SecurityExchange();
    }

    /// Вспомогательная функция: покупка ценной бумаги от конкретного адреса
    function _purchaseSecurityAs(address from, string memory name, uint256 price) private {
        // Для эмуляции, контракт вызывает покупку и меняет владельца вручную
        exchangeToTest.purchaseSecurity(name, price);
        exchangeToTest.exchangeSecurity(exchangeToTest.nextSecurityId() - 1, from);
    }

    // Тестирование покупки ценной бумаги
    function checkPurchaseSecurity() public {
        // Покупаем ценную бумагу от buyer
        _purchaseSecurityAs(buyer, "Stock A", 100);

        // Проверяем данные о купленной ценной бумаге
        (, string memory name, uint256 price, address owner) = exchangeToTest.securities(1);

        Assert.equal(name, "Stock A", "Name should be 'Stock A'");
        Assert.equal(price, 100, "Price should be 100");
        Assert.equal(owner, buyer, "Owner should be buyer's address");

        securityId = 1;
    }

    // Тестирование запрета обмена без владения
    function checkCannotExchangeWithoutOwnership() public {
        // Покупаем ценную бумагу от buyer
        _purchaseSecurityAs(buyer, "Stock C", 300);
        uint256 newSecurityId = exchangeToTest.nextSecurityId() - 1;

        // Попытка обмена от другого адреса (seller = этот контракт)
        try exchangeToTest.exchangeSecurity(newSecurityId, newOwner) {
            Assert.ok(false, "Non-owner should not be able to exchange security");
        } catch Error(string memory reason) {
            Assert.equal(
                reason,
                "You do not own this security",
                "Expected 'You do not own this security' error"
            );
        }
    }
}
