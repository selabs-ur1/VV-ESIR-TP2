import matplotlib.pyplot as plt
import numpy as np

filename = '../reports/report_MaloProject.txt'
simple_filename = filename.split('_')[-1].split('.')[0]

method_names = []
CC_values = []
with open(filename, 'r') as file:
    for line in file:
        CC_values.append(int(line.split()[-1]))

bins = np.arange(min(CC_values) - 0.5, max(CC_values) + 1.5, 1)

plt.figure(figsize=(10, 6))
plt.hist(CC_values, bins=bins, color='skyblue', edgecolor='black', rwidth=0.8)

plt.title('Cyclomatic Complexity Distribution for ' + simple_filename, fontsize=16)
plt.xlabel('Cyclomatic Complexity', fontsize=14)
plt.ylabel('Frequency', fontsize=14)

plt.grid(True, linestyle='--', alpha=0.6)

plt.xticks(np.arange(min(CC_values), max(CC_values) + 1, 1))

plt.tight_layout()
plt.show()

def count_occurences_total(lst):
    occurrences = {}
    for element in lst:
        if element in occurrences:
            occurrences[element] += 1
        else:
            occurrences[element] = 1
    return occurrences, sum(occurrences.values())

dic, total_nb_of_classes = count_occurences_total(CC_values)
print("===", simple_filename, "===")
print("Number of classes with a given CC value", dic)
print("Number of classes with a CC value equal to 1:", dic[1], "(=", round(dic[1]/total_nb_of_classes*100, 1), "%)")
print("Number of classes with a CC value equal to 2:", dic[2], "(=", round(dic[2]/total_nb_of_classes*100, 1), "%)")
print("Number of classes with a CC value equal to 3:", dic[3], "(=", round(dic[3]/total_nb_of_classes*100, 1), "%)")
