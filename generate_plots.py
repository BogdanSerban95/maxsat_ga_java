import matplotlib.pyplot as plt
import argparse


def plot_data(file_name, title, xlabel, ylabel):
    with open(file_name, 'r') as input_file:
        lines = input_file.readlines()
        labels = []
        values = []
        for line in lines:
            split = line[:len(line) - 1].split(',')
            labels.append(split[0])
            values.append([int(x) for x in split[1:]])
        plt.boxplot(values, labels=labels)
        plt.ylabel(ylabel)
        plt.xlabel(xlabel)
        # plt.grid(color='b', linestyle='-', linewidth=0.5)
        plt.title(title)
        plt.show()


parser = argparse.ArgumentParser(description='Genetic algorithm experiment.')
parser.add_argument('-experiment', help='Experiment number', type=int, required=True)

args = parser.parse_args()
experiment = args.experiment

if experiment == 1:
    plot_data('results/population_size.csv', 'Satisfied clauses vs population size', 'Population size',
              'Satisfied clauses')
elif experiment == 2:
    plot_data('results/mutation_rate.csv', 'Satisfied clauses vs mutation rate', 'Mutation rate',
              'Satisfied clauses')
elif experiment == 3:
    plot_data('results/tournament_size.csv', 'Satisfied clauses vs tournament size', 'Tournament size',
              'Satisfied clauses')
