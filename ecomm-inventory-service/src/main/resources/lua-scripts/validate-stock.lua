-- KEYS = list of product quantity keys
-- ARGV = list of required quantities in same order

local results = {}
for i = 1, #KEYS do
    local currentStock = tonumber(redis.call("GET", KEYS[i]))
    local requiredQty = tonumber(ARGV[i])

    table.insert(results, KEYS[i])
    if currentStock == nil then
        table.insert(results, '-2')
    elseif currentStock < requiredQty then
        if currentStock == 0 then
            table.insert(results, '-1')
        else
            table.insert(results, '0')
        end
    else
        table.insert(results, '1')
    end
end
return results
