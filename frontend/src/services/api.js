const API_BASE = "http://localhost:8080";

const getToken = () => sessionStorage.getItem("jwt");
console.log("Token:", getToken());

const fetchWithAuth = (url, options = {}) => {
    return fetch(`${API_BASE}${url}`,{
    ...options,
    headers: {
                    "Content-Type": "application/json",
                    Authorization: `Bearer ${getToken()}`,
                  }
    })
    }
export const getUserBalance = () =>
  fetchWithAuth("/users/balance").then((res) => res.json());

export const getUserHoldings = () =>
  fetchWithAuth("/holdings?userId=1").then((res) => res.json());
export const getTransactions = () =>
  fetchWithAuth("/transactions").then((res) => res.json());

export const postTransaction = (body) =>
  fetchWithAuth("/transactions", {
    method: "POST",
    body: JSON.stringify(body),
  }).then(async (res) => {
    const text = await res.text();
    if (!res.ok) {
      throw new Error(text || "Transaction failed");
    }
    return text;
  });
