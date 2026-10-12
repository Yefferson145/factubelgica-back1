-- Usuarios
CREATE TABLE users (
    user_id UUID PRIMARY KEY,
    username TEXT NOT NULL,
    email TEXT UNIQUE NOT NULL,
    pwd TEXT NOT NULL,
    active BOOLEAN DEFAULT true,
    role TEXT NOT NULL CHECK (role IN ('user', 'admin')) DEFAULT 'user',
    created TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Clientes
CREATE TABLE clients (
    client_id SERIAL PRIMARY KEY,
    vat_number TEXT NOT NULL UNIQUE,
    active BOOLEAN DEFAULT true,
    created TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Histórico de datos de clientes
CREATE TABLE clients_mutable (
    info_id SERIAL PRIMARY KEY,
    client_id INTEGER REFERENCES clients(client_id) ON DELETE RESTRICT,
    legal_name TEXT NOT NULL,
    email TEXT,
    address TEXT,
    phone TEXT,
    created TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Productos
CREATE TABLE products (
    product_id SERIAL PRIMARY KEY,
    description TEXT NOT NULL,
    net_price DECIMAL(12,2) NOT NULL,
    active BOOLEAN DEFAULT true,
    created TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Facturas
CREATE TABLE invoices (
    invoice_id UUID PRIMARY KEY,
    info_id INTEGER REFERENCES clients_mutable(info_id) ON DELETE RESTRICT,
    user_id UUID REFERENCES users(user_id) ON DELETE RESTRICT,
    invoice_number INTEGER, -- Manejado por el TRIGGER
    issue_date DATE NOT NULL DEFAULT CURRENT_DATE,
    due_date DATE NOT NULL,
    vat_rate DECIMAL(5,2) DEFAULT 0.21,
    payment_date DATE DEFAULT NULL, -- Solo se llena cuando status = 'Pagado'
    status TEXT NOT NULL CHECK (status IN ('Pendiente', 'Pagado', 'Enviado', 'Anulado')),
    created TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    -- Garantiza que no se repita el número de factura en el mismo año
    UNIQUE(invoice_number, issue_date)
);

-- Detalle de Facturas
CREATE TABLE invoice_items (
    item_id SERIAL PRIMARY KEY,
    invoice_id UUID REFERENCES invoices(invoice_id) ON DELETE CASCADE,
    product_id INTEGER REFERENCES products(product_id) ON DELETE RESTRICT,
    quantity DECIMAL(12,2) NOT NULL,
    description TEXT NOT NULL,
    net_price DECIMAL(12,2) NOT NULL
);

-- Notas de Crédito
CREATE TABLE credit_notes (
    note_id UUID PRIMARY KEY,
    invoice_id UUID REFERENCES invoices(invoice_id) ON DELETE RESTRICT,
    note_number INTEGER, -- Manejado por su TRIGGER
    issue_date DATE NOT NULL DEFAULT CURRENT_DATE,
    reason TEXT,
    created TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    UNIQUE(note_number, issue_date) -- Garantiza unicidad por año
);

CREATE OR REPLACE FUNCTION fn_assign_invoice_number()
RETURNS TRIGGER AS $$
BEGIN
    -- Solo asignamos número si el registro es nuevo y no se ha provisto uno
    IF NEW.invoice_number IS NULL THEN
        SELECT COALESCE(MAX(invoice_number), 0) + 1
        INTO NEW.invoice_number
        FROM invoices
        WHERE EXTRACT(YEAR FROM issue_date) = EXTRACT(YEAR FROM NEW.issue_date);
    END IF;
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER tr_invoices_before_insert
BEFORE INSERT ON invoices
FOR EACH ROW
EXECUTE FUNCTION fn_assign_invoice_number();

-- 1. Función para numerar notas de crédito
CREATE OR REPLACE FUNCTION fn_assign_credit_note_number()
RETURNS TRIGGER AS $$
BEGIN
    IF NEW.note_number IS NULL THEN
        SELECT COALESCE(MAX(note_number), 0) + 1
        INTO NEW.note_number
        FROM credit_notes
        WHERE EXTRACT(YEAR FROM issue_date) = EXTRACT(YEAR FROM NEW.issue_date);
    END IF;
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

-- 2. Trigger vinculado a la tabla credit_notes
CREATE TRIGGER tr_credit_notes_before_insert
BEFORE INSERT ON credit_notes
FOR EACH ROW
EXECUTE FUNCTION fn_assign_credit_note_number();