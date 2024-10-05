import matplotlib.pyplot as plt
import numpy as np

filename = '../reports/report_MaloProject.txt'

method_names = []
CC_values = []
with open(filename, 'r') as file:
    for line in file:
        CC_values.append(int(line.split()[-1]))

bins = np.arange(min(CC_values) - 0.5, max(CC_values) + 1.5, 1)

plt.figure(figsize=(10, 6))
plt.hist(CC_values, bins=bins, color='skyblue', edgecolor='black', rwidth=0.8)

plt.title('Cyclomatic Complexity Distribution for ' + filename.split('_')[-1].split('.')[0], fontsize=16)
plt.xlabel('Cyclomatic Complexity', fontsize=14)
plt.ylabel('Frequency', fontsize=14)

plt.grid(True, linestyle='--', alpha=0.6)

plt.xticks(np.arange(min(CC_values), max(CC_values) + 1, 1))

plt.tight_layout()
plt.show()