-- Add new fields to users table
ALTER TABLE users
ADD COLUMN apellido_materno VARCHAR(100),
ADD COLUMN edad INT,
ADD COLUMN dni VARCHAR(8) UNIQUE,
ADD COLUMN telefono VARCHAR(9);

-- Add indexes for better performance on search operations
CREATE INDEX idx_users_apellido_materno ON users(apellido_materno);
CREATE INDEX idx_users_dni ON users(dni);
CREATE INDEX idx_users_edad ON users(edad);
CREATE INDEX idx_users_telefono ON users(telefono);