function isJWT(token: string): boolean {
    const parts = token.split('.');
    return parts.length === 3;
}

// Modificado para retornar um objeto com a validade e a role
function isJWTValid(token: string): { isValid: boolean; role?: string } {
    if (!isJWT(token)) {
        console.log('Token não segue a estrutura de um JWT.');
        return { isValid: false };
    }

    try {
        const payloadBase64 = token.split('.')[1];
        const decodedPayload = JSON.parse(atob(payloadBase64));
        
        const now = Date.now() / 1000; // Tempo atual em segundos
        if (decodedPayload.exp < now) {
            console.log('Token expirou.');
            return { isValid: false };
        }

        // Inclui a role no objeto de retorno se existir no payload
        return { isValid: true, role: decodedPayload.role };
    } catch (error) {
        console.error('Falha ao decodificar o token:', error);
        return { isValid: false };
    }
}

export default isJWTValid;
